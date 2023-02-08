package kr.co.wingle.member;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ApiResponse<SignupResponseDto> signup(@ModelAttribute @Valid SignupRequestDto signupRequestDto) {
		SignupResponseDto response = authService.signup(signupRequestDto);
		return ApiResponse.success(SuccessCode.SIGNUP_SUCCESS, response);
	}
}
