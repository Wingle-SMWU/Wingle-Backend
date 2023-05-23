package kr.co.wingle.profile;

import static org.bouncycastle.asn1.nist.NISTObjectIdentifiers.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.dto.InterestsRequestDto;
import kr.co.wingle.profile.dto.InterestsResponseDto;
import kr.co.wingle.profile.dto.IntroductionRequestDto;
import kr.co.wingle.profile.dto.IntroductionResponseDto;
import kr.co.wingle.profile.dto.LanguagesRequestDto;
import kr.co.wingle.profile.dto.LanguagesResponseDto;
import kr.co.wingle.profile.dto.ProfileGetResponseDto;
import kr.co.wingle.profile.dto.ProfileRegistrationResponseDto;
import kr.co.wingle.profile.dto.ProfileRequestDto;
import kr.co.wingle.profile.dto.ProfileResponseDto;
import kr.co.wingle.profile.dto.ProfileViewResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
	private final ProfileService profileService;
	private final AuthService authService;
	private final AES256Util aes;

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

	@GetMapping("")
	public ApiResponse<ProfileGetResponseDto> getProfile() {
		ProfileGetResponseDto response = profileService.getProfile();
		return ApiResponse.success(SuccessCode.PROFILE_READ_SUCCESS, response);
	}

	@GetMapping("/registration")
	public ApiResponse<ProfileRegistrationResponseDto> isRegister() {
		ProfileRegistrationResponseDto response = profileService.isRegister();
		return ApiResponse.success(SuccessCode.PROFILE_REGISTER_READ_SUCCESS, response);
	}

	@GetMapping("/detail")
	public ApiResponse<ProfileViewResponseDto> viewProfile() {
		ProfileViewResponseDto response = profileService.getProfileDetail();
		return ApiResponse.success(SuccessCode.PROFILE_READ_SUCCESS, response);
	}

	@GetMapping("/{id}")
	public ApiResponse<ProfileGetResponseDto> getUserProfile(@PathVariable String id) throws NoSuchAlgorithmException,
		UnsupportedEncodingException, GeneralSecurityException {
		id = aes.encrypt(id);
		System.out.println("암호화된 키 = "+id);
		ProfileGetResponseDto response = profileService.getUserProfile(id);
		return ApiResponse.success(SuccessCode.PROFILE_READ_SUCCESS, response);

	}
}
