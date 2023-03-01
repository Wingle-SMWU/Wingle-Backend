package kr.co.wingle.profile.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestsResponseDto {
	private List<String> interests;

	public static InterestsResponseDto of(List<String> interests) {
		InterestsResponseDto interestsResponseDto = new InterestsResponseDto();
		interestsResponseDto.interests = interests;
		return interestsResponseDto;
	}
}
