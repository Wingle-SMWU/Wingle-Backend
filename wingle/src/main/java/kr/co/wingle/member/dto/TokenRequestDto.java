package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRequestDto {
	@NotBlank
	private String refreshToken;

	public static TokenRequestDto of(String refreshToken) {
		TokenRequestDto tokenRequestDto = new TokenRequestDto();
		tokenRequestDto.refreshToken = refreshToken;
		return tokenRequestDto;
	}
}
