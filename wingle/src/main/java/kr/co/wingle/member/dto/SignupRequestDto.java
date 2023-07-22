package kr.co.wingle.member.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.wingle.affliation.entity.School;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequestDto {

	@NotBlank(message = "파일 url이 없습니다.")
	private String idCardImageUrl;

	@Email
	@NotBlank(message = "이메일이 없습니다.")
	private String email;

	@NotBlank(message = "비밀번호가 없습니다.")
	private String password;

	@NotBlank(message = "이름이 없습니다.")
	private String name;

	@AssertTrue(message = "닉네임 중복 검사를 통과해야 합니다.")
	private boolean isNicknameChecked;

	@NotBlank(message = "닉네임이 없습니다.")
	private String nickname;

	@NotNull
	private boolean gender;

	@NotBlank(message = "국가 정보가 없습니다.")
	private String nation;

	@AssertTrue(message = "약관에 필수로 동의해야 합니다.")
	private boolean termsOfUse;

	@AssertTrue(message = "약관에 필수로 동의해야 합니다.")
	private boolean termsOfPersonalInformation;

	@NotNull
	private boolean termsOfPromotion;

	@NotNull(message = "학교 Id가 없습니다.")
	private Long schoolId;

	@NotBlank(message = "학과가 없습니다.")
	private String department;

	@NotBlank(message = "학번이 없습니다.")
	private String studentNumber;

	public Member toMember(PasswordEncoder passwordEncoder, School school) {
		return Member.createMember(name, idCardImageUrl, email, passwordEncoder.encode(password), Authority.ROLE_USER,
			school, department, studentNumber);
	}
}

