package kr.co.wingle.member;

import java.util.regex.Pattern;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.common.jwt.TokenProvider;
import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.common.util.SecurityUtil;
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

	@Transactional
	public SignupResponseDto signup(SignupRequestDto request) {
		String email = request.getEmail();
		validEmail(email);
		checkDuplicateEmail(email);
		//닉네임 중복 검사 추후 구현

		String idCardImageUrl = uploadIdCardImage(request.getIdCardImage());

		Member member = request.toMember(idCardImageUrl, passwordEncoder);
		memberRepository.save(member);

		//Profile 테이블에 유저 정보 저장 추후 구현

		return SignupResponseDto.of(1L, member.getName(), "nickname");
	}

	@Transactional(readOnly = true)
	public Member findMember() {
		return memberRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	private void validEmail(String email) {
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		if (!emailPattern.matcher(email).matches()) {
			throw new CustomException(ErrorCode.EMAIL_BAD_REQUEST);
		}
	}

	private void checkDuplicateEmail(String email) {
		// interface 구현으로 추후 수정
		if (memberRepository.existsByEmail(email)) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}
	}

	private String uploadIdCardImage(MultipartFile idCardImage) {
		return s3Util.idCardImageUpload(idCardImage);
	}
}
