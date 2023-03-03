package kr.co.wingle.profile.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileRegistrationResponseDto {
	private Boolean registration;

	public static ProfileRegistrationResponseDto of(Boolean registration) {
		ProfileRegistrationResponseDto profileRegistrationResponseDto = new ProfileRegistrationResponseDto();
		profileRegistrationResponseDto.registration = registration;
		return profileRegistrationResponseDto;
	}
}
