package kr.co.wingle.member;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.LogoutRequestDto;
import kr.co.wingle.member.dto.MemberResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.EmailRequestDto;
import kr.co.wingle.member.dto.EmailResponseDto;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.MemberResponseDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.TokenRequestDto;
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

	@PostMapping("/login")
	public ApiResponse<TokenDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
		TokenDto response = authService.login(loginRequestDto);
		return ApiResponse.success(SuccessCode.LOGIN_SUCCESS, response);
	}

	@GetMapping("/me")
	public ApiResponse<MemberResponseDto> getMyAccount() {
		Member member = authService.findMember();
		MemberResponseDto response = MemberResponseDto.from(member);
		return ApiResponse.success(SuccessCode.ACCOUNT_READ_SUCCESS, response);
	}

	@PostMapping("/refresh")
	public ApiResponse<TokenDto> refresh(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
		TokenDto response = authService.reissue(tokenRequestDto);
		return ApiResponse.success(SuccessCode.TOKEN_REISSUE_SUCCESS, response);
	}

	@GetMapping("/logout")
	public ApiResponse<Object> logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto) {
		authService.logout(logoutRequestDto);
		return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS, null);
	}

	@PostMapping("/email")
	public ApiResponse<EmailResponseDto> email(@RequestBody @Valid EmailRequestDto emailRequestDto) {
		EmailResponseDto response = authService.sendEmailCode(emailRequestDto);
		return ApiResponse.success(SuccessCode.EMAIL_SEND_SUCCESS, response);
	}
}
