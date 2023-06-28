package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailResponseDto {
	private String certificationKey;
	private int requestCount;

	public static EmailResponseDto of(String certificationKey, int requestCount) {
		EmailResponseDto emailResponseDto = new EmailResponseDto();
		emailResponseDto.certificationKey = certificationKey;
		emailResponseDto.requestCount = requestCount;
		return emailResponseDto;
	}
}
