package kr.co.wingle.profile.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileViewResponseDto {

	private String image;
	private String nation;
	private String nickname;
	private Boolean gender;
	private List<LanguageDto> languages;
	private List<String> interests;
	private String introduce;
	private String sns;
	private String schoolName;

	public static ProfileViewResponseDto of(String image, String nation, String nickname, Boolean gender,
		List<LanguageDto> languages, List<String> interests, String introduce,
		String sns, String schoolName) {
		ProfileViewResponseDto profileViewResponseDto = new ProfileViewResponseDto();
		profileViewResponseDto.image = image;
		profileViewResponseDto.nation = nation;
		profileViewResponseDto.nickname = nickname;
		profileViewResponseDto.gender = gender;
		profileViewResponseDto.languages = languages;
		profileViewResponseDto.interests = interests;
		profileViewResponseDto.introduce = introduce;
		profileViewResponseDto.sns = sns;
		profileViewResponseDto.schoolName = schoolName;

		return profileViewResponseDto;
	}

}
