package kr.co.wingle.profile.dto;

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

	public static ProfileGetResponseDto of(String image, String nickname, Boolean gender, String nation) {
		ProfileGetResponseDto profileGetResponseDto = new ProfileGetResponseDto();
		profileGetResponseDto.image = image;
		profileGetResponseDto.nickname = nickname;
		profileGetResponseDto.gender = gender;
		profileGetResponseDto.nation = nation;
		return profileGetResponseDto;
	}

}
