package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {
	private String grantType;

	private String accessToken;

	private String refreshToken;

	private boolean isAdmin;

	public static LoginResponseDto from(TokenDto tokenDto, boolean isAdmin) {
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.grantType = tokenDto.getGrantType();
		loginResponseDto.accessToken = tokenDto.getAccessToken();
		loginResponseDto.refreshToken = tokenDto.getRefreshToken();
		loginResponseDto.isAdmin = isAdmin;
		return loginResponseDto;
	}
}
