package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogoutRequestDto {
	@NotBlank
	private String accessToken;

	@NotBlank
	private String refreshToken;

	public static LogoutRequestDto of(String accessToken, String refreshToken) {
		LogoutRequestDto logoutRequestDto = new LogoutRequestDto();
		logoutRequestDto.accessToken = accessToken;
		logoutRequestDto.refreshToken = refreshToken;
		return logoutRequestDto;
	}
}
