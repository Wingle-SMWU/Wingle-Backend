package kr.co.wingle.member.service;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
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
import kr.co.wingle.member.dto.CertificationRequestDto;
import kr.co.wingle.member.dto.CertificationResponseDto;
import kr.co.wingle.member.dto.EmailRequestDto;
import kr.co.wingle.member.dto.EmailResponseDto;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.LogoutRequestDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.TokenRequestDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final PasswordEncoder passwordEncoder;
	private final S3Util s3Util;
	private final RedisUtil redisUtil;

	@Autowired
	MailService mailService;

	@Transactional
	public SignupResponseDto signup(SignupRequestDto request) {
		String email = request.getEmail();
		validateEmail(email);
		checkDuplicateEmail(email);
		// TODO: 닉네임 중복 검사
		// TODO: 약관 데이터 넣고 TermMember 저장

		String idCardImageUrl = uploadIdCardImage(request.getIdCardImage());

		Member member = request.toMember(idCardImageUrl, passwordEncoder);
		memberRepository.save(member);

		// TODO: Profile 테이블에 유저 정보 저장

		return SignupResponseDto.of(member.getId(), member.getName(), "nickname");
	}

	@Transactional
	public TokenDto login(LoginRequestDto loginRequestDto) {
		String email = loginRequestDto.getEmail();
		validateEmail(email);
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

		UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenDto tokenDto = getRedisTokenKey(authentication);
		tokenDto.setAdmin(member.getAuthority() == Authority.ROLE_ADMIN);
		return tokenDto;
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

	private void validateEmail(String email) {
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		if (!emailPattern.matcher(email).matches()) {
			throw new CustomException(ErrorCode.EMAIL_BAD_REQUEST);
		}
	}

	private void checkDuplicateEmail(String email) {
		// TODO: interface 구현
		if (memberRepository.existsByEmail(email)) {
			throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
		}
	}

	private String uploadIdCardImage(MultipartFile idCardImage) {
		return s3Util.idCardImageUpload(idCardImage);
	}

	public EmailResponseDto sendEmailCode(EmailRequestDto emailRequestDto) {
		String to = emailRequestDto.getEmail();
		String certificationKey = mailService.sendEmailCode(to);
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
}
