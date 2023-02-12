package kr.co.wingle.member;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.util.RedisUtil;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.common.jwt.TokenProvider;
import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.common.util.SecurityUtil;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
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
		memberRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		return getTokenDto(loginRequestDto);
	}

	@Transactional(readOnly = true)
	public Member findMember() {
		return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	private TokenDto getTokenDto(LoginRequestDto loginRequestDto) {
		UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

		String refreshTokenKey = generateUUID();
		String key = RedisUtil.PREFIX_REFRESH_TOKEN + authentication.getName() + refreshTokenKey;
		redisUtil.setDataExpire(key, tokenDto.getRefreshToken(), 10L);

		tokenDto.setRefreshToken(refreshTokenKey);
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
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}
	}

	private String uploadIdCardImage(MultipartFile idCardImage) {
		return s3Util.idCardImageUpload(idCardImage);
	}
}