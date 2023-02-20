package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
	private String grantType;

	private String accessToken;

	@Setter
	private String refreshToken;

	public static TokenDto of(String grantType, String accessToken, String refreshToken) {
		TokenDto tokenDto = new TokenDto();
		tokenDto.grantType = grantType;
		tokenDto.accessToken = accessToken;
		tokenDto.refreshToken = refreshToken;
		return tokenDto;
	}
}
