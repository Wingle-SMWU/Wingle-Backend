package kr.co.wingle.profile.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileGetResponseDto {
	private String image;
	private String nickname;
	private Boolean gender;
	private String nation;

	private List<LanguageDto> languages;

	private String introduction;

	private List<String> interests;
	private String schoolName;

	public static ProfileGetResponseDto of(
		String image, String nickname, Boolean gender, String nation,
		List<LanguageDto> languages,
		String introduction, List<String> interests,
		String schoolName
	) {
		ProfileGetResponseDto profileGetResponseDto = new ProfileGetResponseDto();
		profileGetResponseDto.image = image;
		profileGetResponseDto.nickname = nickname;
		profileGetResponseDto.gender = gender;
		profileGetResponseDto.nation = nation;
		profileGetResponseDto.languages = languages;
		profileGetResponseDto.introduction = introduction;
		profileGetResponseDto.interests = interests;
		profileGetResponseDto.schoolName = schoolName;
		return profileGetResponseDto;
	}

}
