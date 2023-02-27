package kr.co.wingle.profile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.MemberResponseDto;
import kr.co.wingle.member.entity.Member;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProfileController {
	private final ProfileService profileServie;

	@PostMapping("/profile")
	public ApiResponse<ProfileResponseDto> saveProfile(@RequestBody ProfileRequestDto profileRequestDto){
		ProfileResponseDto response = profileServie.save(profileRequestDto);
		return ApiResponse.success(SuccessCode.EXAMPLE_SUCCESS,response);
	}

	// @GetMapping("/profile")
	// public ApiResponse<ProfileResponseDto> getProfile(){
	// 	ProfileResponseDto response=
	// 	return ApiResponse.success(SuccessCode.EXAMPLE_SUCCESS, response);
	// }
}
