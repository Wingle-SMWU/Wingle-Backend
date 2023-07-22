package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdCardImageResponseDto {
	private String idCardImageName;
	private String idCardImageUrl;

	public static IdCardImageResponseDto of(String idCardImageName, String idCardImageUrl) {
		IdCardImageResponseDto responseDto = new IdCardImageResponseDto();
		responseDto.idCardImageName = idCardImageName;
		responseDto.idCardImageUrl = idCardImageUrl;
		return responseDto;
	}
}
