package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailResponseDto {
	private String certificationKey;

	public static EmailResponseDto of(String certificationKey) {
		EmailResponseDto emailResponseDto = new EmailResponseDto();
		emailResponseDto.certificationKey = certificationKey;
		return emailResponseDto;
	}
}
