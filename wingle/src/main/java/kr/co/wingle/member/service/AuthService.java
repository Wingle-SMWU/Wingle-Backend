package kr.co.wingle.member.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.common.exception.DuplicateException;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.jwt.TokenInfo;
import kr.co.wingle.common.jwt.TokenProvider;
import kr.co.wingle.common.util.RedisUtil;
import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.common.util.SecurityUtil;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.TermMemberRepository;
import kr.co.wingle.member.TermRepository;
import kr.co.wingle.member.dto.AcceptanceRequestDto;
import kr.co.wingle.member.dto.CertificationRequestDto;
import kr.co.wingle.member.dto.CertificationResponseDto;
import kr.co.wingle.member.dto.EmailRequestDto;
import kr.co.wingle.member.dto.EmailResponseDto;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.LoginResponseDto;
import kr.co.wingle.member.dto.LogoutRequestDto;
import kr.co.wingle.member.dto.NicknameRequestDto;
import kr.co.wingle.member.dto.NicknameResponseDto;
import kr.co.wingle.member.dto.PermissionResponseDto;
import kr.co.wingle.member.dto.RejectionRequestDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.TokenRequestDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.entity.Permission;
import kr.co.wingle.member.entity.Term;
import kr.co.wingle.member.entity.TermCode;
import kr.co.wingle.member.entity.TermMember;
import kr.co.wingle.member.mailVo.AcceptanceMail;
import kr.co.wingle.member.mailVo.ApplyMail;
import kr.co.wingle.member.mailVo.CodeMail;
import kr.co.wingle.member.mailVo.RejectionMail;
import kr.co.wingle.profile.ProfileRepository;
import kr.co.wingle.profile.entity.Profile;
import kr.co.wingle.profile.util.ProfileUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;
	private final TermRepository termRepository;
	private final TermMemberRepository termMemberRepository;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final PasswordEncoder passwordEncoder;
	private final S3Util s3Util;
	private final RedisUtil redisUtil;
	private final MailService mailService;
	private final MemberService memberService;
	private final ProfileUtil profileUtil;

	@Transactional
	public SignupResponseDto signup(SignupRequestDto request) {
		String email = request.getEmail();
		if (!isSignupAvailableEmail(email)) {
			throw new DuplicateException(ErrorCode.SIGNUP_UNAVAILABLE_EMAIL);
		}
		// 재가입인 경우에도 예전에 썼던 닉네임 다시 쓸 수 없음
		if (profileUtil.isDuplicatedNickname(request.getNickname())) {
			throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
		}

		// TODO: S3 저장 로직 다른 API로 분리
		// upload S3
		String idCardImageUrl = uploadIdCardImage(request.getIdCardImage());

		// 회원, 프로필 객체 생성
		Member member = request.toMember(idCardImageUrl, passwordEncoder);
		Profile profile = Profile.createProfile(member, request.getNickname(), request.isGender(), request.getNation());

		if (memberRepository.existsByEmail(email)) { // 거절됐던 회원인 경우
			// save member
			Member existMember = memberRepository.findByEmail(email).get();
			member = Member.copyMember(member, existMember);

			// save profile
			Profile existProfile = profileRepository.findByMember(member).get();
			profile = Profile.copyProfile(profile, existProfile);
		} else { // 신규 회원
			memberRepository.save(member);
			profileRepository.save(profile);
		}

		// save termMember
		getTermAndSaveTermMember(member, TermCode.TERMS_OF_USE, request.isTermsOfUse());
		getTermAndSaveTermMember(member, TermCode.TERMS_OF_PERSONAL_INFORMATION,
			request.isTermsOfPersonalInformation());
		getTermAndSaveTermMember(member, TermCode.TERMS_OF_PROMOTION, request.isTermsOfPromotion());

		// send mail
		mailService.sendEmail(member.getEmail(), new ApplyMail(member.getName()));

		return SignupResponseDto.of(member.getId(), member.getName(), profile.getNickname());
	}

	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		String email = loginRequestDto.getEmail();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

		UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenDto tokenDto = getRedisTokenKey(authentication);
		return LoginResponseDto.from(tokenDto, member.getAuthority() == Authority.ROLE_ADMIN);
	}

	@Transactional(readOnly = true)
	public Member findLoggedInMember() {
		return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Member findAcceptedLoggedInMember() {
		Member member = findLoggedInMember();
		if (!memberService.isAcceptedMember(member.getId())) {
			throw new ForbiddenException(ErrorCode.NOT_ACCEPTED);
		}
		return member;
	}

	@Transactional
	public TokenDto reissue(TokenRequestDto tokenRequestDto) {
		String key = RedisUtil.PREFIX_REFRESH_TOKEN + tokenRequestDto.getRefreshToken();
		String refreshToken = redisUtil.getData(key);
		if (refreshToken == null) {
			throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
		}
		redisUtil.deleteData(key);

		Authentication authentication = tokenProvider.getAuthentication(refreshToken);
		return getRedisTokenKey(authentication);
	}

	@Transactional
	public void logout(LogoutRequestDto logoutRequestDto) {
		String accessToken = logoutRequestDto.getAccessToken();
		if (!tokenProvider.validateToken(accessToken)) {
			throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
		}

		String refreshTokenKey = RedisUtil.PREFIX_REFRESH_TOKEN + logoutRequestDto.getRefreshToken();

		String refreshToken = redisUtil.getData(refreshTokenKey);
		if (refreshToken == null) {
			throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
		}
		redisUtil.deleteData(refreshTokenKey);

		redisUtil.setDataExpire(RedisUtil.PREFIX_LOGOUT + accessToken, "logout", TokenInfo.ACCESS_TOKEN_EXPIRE_TIME);
	}

	public EmailResponseDto sendCodeMail(EmailRequestDto emailRequestDto) {
		String to = emailRequestDto.getEmail();
		if (!isSignupAvailableEmail(to)) {
			throw new DuplicateException(ErrorCode.SIGNUP_UNAVAILABLE_EMAIL);
		}

		String attemptEmailKey = RedisUtil.PREFIX_EMAIL + to;
		if (redisUtil.existsData(attemptEmailKey)) {
			int count = Integer.parseInt(redisUtil.getData(attemptEmailKey));
			redisUtil.updateData(attemptEmailKey, String.valueOf(++count));
		} else {
			redisUtil.setDataExpire(attemptEmailKey, String.valueOf(1), 1000 * 60 * 60 * 24L);
		}

		if (Integer.parseInt(redisUtil.getData(attemptEmailKey)) > 5) {
			throw new CustomException(ErrorCode.TOO_MANY_EMAIL_REQUEST);
		}

		String certificationKey = mailService.sendEmail(to, new CodeMail());
		return EmailResponseDto.of(certificationKey);
	}

	public CertificationResponseDto checkEmailAndCode(CertificationRequestDto certificationRequestDto) {
		String email = certificationRequestDto.getCertificationKey();
		String inputCode = certificationRequestDto.getCertificationCode();
		String code = redisUtil.getData(email);
		if (code == null)
			throw new CustomException(ErrorCode.NO_EMAIL_CODE);
		if (!code.equals(inputCode))
			throw new CustomException(ErrorCode.INCONSISTENT_CODE);
		return CertificationResponseDto.of(true);
	}

	@Transactional
	public PermissionResponseDto sendAcceptanceMail(AcceptanceRequestDto acceptanceRequestDto) {
		Long userId = acceptanceRequestDto.getUserId();
		Member member = memberService.findMemberByMemberId(userId);
		if (member.getPermission() == Permission.APPROVE.getStatus())
			throw new CustomException(ErrorCode.ALREADY_ACCEPTANCE);

		member.setPermission(Permission.APPROVE.getStatus());
		mailService.sendEmail(member.getEmail(), new AcceptanceMail(member.getName()));
		return PermissionResponseDto.of(userId, true);
	}

	@Transactional
	public PermissionResponseDto sendRejectionMail(RejectionRequestDto rejectionRequestDto) {
		Long userId = rejectionRequestDto.getUserId();
		Member member = memberService.findMemberByMemberId(userId);
		if (member.getPermission() == Permission.DENY.getStatus())
			throw new CustomException(ErrorCode.ALREADY_DENY);
		if (member.getPermission() == Permission.APPROVE.getStatus())
			throw new CustomException(ErrorCode.ALREADY_ACCEPTANCE);

		member.setPermission(Permission.DENY.getStatus());
		memberService.saveRejectionReason(rejectionRequestDto);
		mailService.sendEmail(member.getEmail(), new RejectionMail(rejectionRequestDto.getReason()));
		return PermissionResponseDto.of(userId, false);
	}

	private void getTermAndSaveTermMember(Member member, TermCode termCode, boolean agreement) {
		Optional<Term> termOptional = termRepository.findByName(termCode.getName());
		if (termOptional.isEmpty()) {
			Term term = Term.createTerm(termCode.getCode(), termCode.getName(), termCode.isNecessity());
			termRepository.save(term);
			saveTermMember(term, member, agreement);
		} else {
			saveTermMember(termOptional.get(), member, agreement);
		}
	}

	private void saveTermMember(Term term, Member member, boolean agreement) {
		Optional<TermMember> termMemberOptional = termMemberRepository.findByMemberAndTerm(member, term);
		if (termMemberOptional.isPresent()) { // 가입 거절된 회원
			TermMember termMember = termMemberOptional.get();
			termMember.setAgreement(agreement);
		} else { // 신규 회원
			TermMember termMember = TermMember.createTermMember(term, member, agreement);
			termMemberRepository.save(termMember);
		}
	}

	private TokenDto getRedisTokenKey(Authentication authentication) {
		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

		String uuid = generateUUID();
		String refreshTokenKey = RedisUtil.PREFIX_REFRESH_TOKEN + uuid;
		while (redisUtil.getData(refreshTokenKey) != null) {
			uuid = generateUUID();
			refreshTokenKey = RedisUtil.PREFIX_REFRESH_TOKEN + uuid;
		}
		redisUtil.setDataExpire(refreshTokenKey, tokenDto.getRefreshToken(), TokenInfo.REFRESH_TOKEN_EXPIRE_TIME);

		tokenDto.setRefreshToken(uuid);
		return tokenDto;
	}

	private String generateUUID() {
		return UUID.randomUUID().toString().substring(0, 6);
	}

	private boolean isSignupAvailableEmail(String email) {
		Member member = memberRepository.findByEmail(email).orElseGet(() -> null);
		// 중복된 이메일 없음
		if (member == null) {
			return true;
		}

		// 이미 거절되어 재가입하려는 이메일
		if (Permission.DENY.getStatus() == member.getPermission()) {
			return true;
		}

		return false;
	}

	public NicknameResponseDto checkDuplicateNickname(NicknameRequestDto request) {
		String nickname = request.getNickname();
		if (profileRepository.existsByNickname(nickname)) {
			throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
		}
		return NicknameResponseDto.of(true);
	}

	private String uploadIdCardImage(MultipartFile idCardImage) {
		return s3Util.idCardImageUpload(idCardImage);
	}
}
