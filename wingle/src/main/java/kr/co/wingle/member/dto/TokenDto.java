package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {

	private String accessToken;

	@Setter
	private String refreshToken;

	@Setter
	private boolean isAdmin;

	public static TokenDto of(String accessToken, String refreshToken) {
		TokenDto tokenDto = new TokenDto();
		tokenDto.accessToken = accessToken;
		tokenDto.refreshToken = refreshToken;
		return tokenDto;
	}
}
