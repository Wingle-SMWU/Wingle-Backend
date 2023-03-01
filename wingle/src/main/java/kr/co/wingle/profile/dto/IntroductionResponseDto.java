package kr.co.wingle.profile.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntroductionResponseDto {
	private String introduction;

	public static IntroductionResponseDto of(String introduction) {
		IntroductionResponseDto introductionResponseDto = new IntroductionResponseDto();
		introductionResponseDto.introduction = introduction;
		return introductionResponseDto;
	}
}
