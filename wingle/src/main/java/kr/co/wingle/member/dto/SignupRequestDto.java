package kr.co.wingle.member.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.member.Authority;
import kr.co.wingle.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequestDto {

	@NotNull(message = "파일이 없습니다.")
	private MultipartFile idCardImage;

	@NotBlank(message = "이메일이 없습니다.")
	private String email;

	@NotBlank(message = "비밀번호가 없습니다.")
	private String password;

	@NotBlank(message = "이름이 없습니다.")
	private String name;

	@NotBlank(message = "닉네임이 없습니다.")
	private String nickname;

	@NotNull
	private boolean gender;

	@NotBlank(message = "국가 정보가 없습니다.")
	private String nation;

	@AssertTrue(message = "약관에 필수로 동의해야 합니다.")
	private boolean termsOfUse;

	@AssertTrue(message = "약관에 필수로 동의해야 합니다.")
	private boolean collectionOfPersonalInformation;

	public Member toMember(String idCardImageUrl, PasswordEncoder passwordEncoder) {
		return Member.createMember(name, idCardImageUrl, email, passwordEncoder.encode(password), Authority.ROLE_USER);
	}
}

