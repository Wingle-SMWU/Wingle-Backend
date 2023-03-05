package kr.co.wingle.profile.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileResponseDto {
	private String image;
	private String nickname;

	public static ProfileResponseDto of(String image, String nickname) {
		ProfileResponseDto profileResponseDto = new ProfileResponseDto();
		profileResponseDto.image = image;
		profileResponseDto.nickname = nickname;
		return profileResponseDto;
	}


}
