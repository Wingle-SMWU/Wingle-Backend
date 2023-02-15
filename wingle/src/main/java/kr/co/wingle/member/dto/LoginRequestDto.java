package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
	@NotBlank(message = "이메일이 없습니다.")
	private String email;

	@NotBlank(message = "비밀번호가 없습니다.")
	private String password;

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

	public static LoginRequestDto of(String email, String password) {
		LoginRequestDto loginRequestDto = new LoginRequestDto();
		loginRequestDto.email = email;
		loginRequestDto.password = password;
		return loginRequestDto;
	}
}
