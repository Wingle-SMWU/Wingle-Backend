package kr.co.wingle.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CertificationRequestDto {
	@Email
	@NotBlank(message = "이메일이 없습니다.")
	private String certificationKey;
	@NotBlank(message = "인증정보가 없습니다.")
	private String certificationCode;
}
