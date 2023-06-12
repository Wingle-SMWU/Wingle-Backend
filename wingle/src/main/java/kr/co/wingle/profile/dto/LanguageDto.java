package kr.co.wingle.profile.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LanguageDto {
	private int order;
	private String language;

	public static LanguageDto of(int order, String language) {
		LanguageDto languageDto = new LanguageDto();
		languageDto.order = order;
		languageDto.language = language;
		return languageDto;
	}
}
