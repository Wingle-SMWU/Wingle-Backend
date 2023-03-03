package kr.co.wingle.profile;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.profile.dto.InterestsRequestDto;
import kr.co.wingle.profile.dto.InterestsResponseDto;
import kr.co.wingle.profile.dto.IntroductionRequestDto;
import kr.co.wingle.profile.dto.LanguagesRequestDto;
import kr.co.wingle.profile.dto.LanguagesResponseDto;
import kr.co.wingle.profile.dto.ProfileRequestDto;
import kr.co.wingle.profile.dto.IntroductionResponseDto;
import kr.co.wingle.profile.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
	private final ProfileService profileService;

	@PostMapping("")
	public ApiResponse<ProfileResponseDto> saveProfile(@ModelAttribute @Valid ProfileRequestDto profileRequestDto) {
		ProfileResponseDto response = profileService.saveProfile(profileRequestDto);
		return ApiResponse.success(SuccessCode.PROFILE_SAVE_SUCCESS, response);
	}

	@PostMapping("/languages")
	public ApiResponse<LanguagesResponseDto> saveLanguages(
		@RequestBody @Valid LanguagesRequestDto languagesRequestDto) {
		LanguagesResponseDto response = profileService.saveLanguages(languagesRequestDto);
		return ApiResponse.success(SuccessCode.LANGUAGES_SAVE_SUCCESS, response);
	}

	@PostMapping("/introduction")
	public ApiResponse<IntroductionResponseDto> saveIntroduction(
		@RequestBody @Valid IntroductionRequestDto introductionRequestDto) {
		IntroductionResponseDto response = profileService.saveIntroduction(introductionRequestDto);
		return ApiResponse.success(SuccessCode.INTRODUCTION_SAVE_SUCCESS, response);
	}

	@PostMapping("/interests")
	public ApiResponse<InterestsResponseDto> saveInterests(
		@RequestBody @Valid InterestsRequestDto interestsRequestDto) {
		InterestsResponseDto response = profileService.saveInterests(interestsRequestDto);
		return ApiResponse.success(SuccessCode.INTERESTS_SAVE_SUCCESS, response);
	}

	// @GetMapping("/profile")
	// public ApiResponse<ProfileResponseDto> getProfile(){
	// 	ProfileResponseDto response=
	// 	return ApiResponse.success(SuccessCode.EXAMPLE_SUCCESS, response);
	// }
}
