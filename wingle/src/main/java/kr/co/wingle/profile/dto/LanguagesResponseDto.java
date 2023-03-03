package kr.co.wingle.profile.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LanguagesResponseDto {
	private List<LanguageDto> languages;

	public static LanguagesResponseDto of(List<LanguageDto> languages) {
		LanguagesResponseDto languagesResponseDto = new LanguagesResponseDto();
		languagesResponseDto.languages = languages;
		return languagesResponseDto;
	}

	@Getter
	@NoArgsConstructor
	public static class LanguageDto {
		private int order;
		private String interest;

		public static LanguageDto of(int order, String interest) {
			LanguageDto interestDto = new LanguageDto();
			interestDto.order = order;
			interestDto.interest = interest;
			return interestDto;
		}
	}
}
