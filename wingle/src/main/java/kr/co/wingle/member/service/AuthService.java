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
import kr.co.wingle.member.mailVo.CodeMail;
import kr.co.wingle.member.mailVo.RejectionMail;
import kr.co.wingle.profile.Profile;
import kr.co.wingle.profile.ProfileRepository;
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

	@Transactional
	public SignupResponseDto signup(SignupRequestDto request) {
		String email = request.getEmail();
		checkDuplicateEmail(email);

		// upload S3
		String idCardImageUrl = uploadIdCardImage(request.getIdCardImage());

		// save member
		Member member = request.toMember(idCardImageUrl, passwordEncoder);
		memberRepository.save(member);

		// save termMember
		getTermAndSaveTermMember(member, TermCode.TERMS_OF_USE, request.isTermsOfUse());
		getTermAndSaveTermMember(member, TermCode.TERMS_OF_PERSONAL_INFORMATION,
			request.isTermsOfPersonalInformation());
		getTermAndSaveTermMember(member, TermCode.TERMS_OF_PROMOTION, request.isTermsOfPromotion());

		// save profile
		Profile profile = Profile.createProfile(member, request.getNickname(), request.isGender(), request.getNation());
		profileRepository.save(profile);

		return SignupResponseDto.of(member.getId(), member.getName(), "nickname");
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
	public Member findMember() {
		return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
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
		Member member = validateMember(userId);
		if (member.getPermission() == Permission.APPROVE.getStatus())
			throw new CustomException(ErrorCode.ALREADY_ACCEPTANCE);

		member.setPermission(Permission.APPROVE.getStatus());
		mailService.sendEmail(member.getEmail(), new AcceptanceMail(member.getName()));
		return PermissionResponseDto.of(userId, true);
	}

	@Transactional
	public PermissionResponseDto sendRejectionMail(RejectionRequestDto rejectionRequestDto) {
		Long userId = rejectionRequestDto.getUserId();
		Member member = validateMember(userId);
		if (member.getPermission() == Permission.DENY.getStatus())
			throw new CustomException(ErrorCode.ALREADY_DENY);
		if (member.getPermission() == Permission.APPROVE.getStatus())
			throw new CustomException(ErrorCode.ALREADY_ACCEPTANCE);

		member.setPermission(Permission.DENY.getStatus());
		// TODO: 거절 사유 저장
		mailService.sendEmail(member.getEmail(), new RejectionMail(rejectionRequestDto.getReason()));
		return PermissionResponseDto.of(userId, false);
	}

	private void getTermAndSaveTermMember(Member member, TermCode termCode, boolean agreement) {
		Optional<Term> termOptional = termRepository.findByName(termCode.getName());
		if (termOptional.isEmpty()) {
			Term term = Term.createTerm(termCode.getCode(), termCode.getName(), termCode.isNecessity());
			termRepository.save(term);
			TermMember termMember = TermMember.createTermMember(term, member, agreement);
			termMemberRepository.save(termMember);
		} else {
			TermMember termMember = TermMember.createTermMember(termOptional.get(), member, agreement);
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

	private void checkDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
		}
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

	private Member validateMember(Long userId) {
		return memberRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}
}
