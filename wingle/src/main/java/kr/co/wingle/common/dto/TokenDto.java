package kr.co.wingle.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
	private String grantType;
	private String accessToken;
	private String refreshToken;

	public static TokenDto of(String grantType, String accessToken, String refreshToken) {
		TokenDto tokenDto = new TokenDto();
		tokenDto.grantType = grantType;
		tokenDto.accessToken = accessToken;
		tokenDto.refreshToken = refreshToken;
		return tokenDto;
	}
}
