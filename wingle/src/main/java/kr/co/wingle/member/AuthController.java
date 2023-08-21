package kr.co.wingle.member;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.CertificationRequestDto;
import kr.co.wingle.member.dto.CertificationResponseDto;
import kr.co.wingle.member.dto.EmailRequestDto;
import kr.co.wingle.member.dto.EmailResponseDto;
import kr.co.wingle.member.dto.IdCardImageReqeustDto;
import kr.co.wingle.member.dto.IdCardImageResponseDto;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.LoginResponseDto;
import kr.co.wingle.member.dto.LogoutRequestDto;
import kr.co.wingle.member.dto.MemberResponseDto;
import kr.co.wingle.member.dto.NicknameRequestDto;
import kr.co.wingle.member.dto.NicknameResponseDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.TokenRequestDto;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ApiResponse<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
		SignupResponseDto response = authService.signup(signupRequestDto);
		return ApiResponse.success(SuccessCode.SIGNUP_SUCCESS, response);
	}

	@PostMapping("/login")
	public ApiResponse<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
		LoginResponseDto response = authService.login(loginRequestDto);
		log.info("[{}] login success", loginRequestDto.getEmail());
		return ApiResponse.success(SuccessCode.LOGIN_SUCCESS, response);
	}

	@PostMapping("/idcardimage")
	public ApiResponse<IdCardImageResponseDto> signup(
		@ModelAttribute @Valid IdCardImageReqeustDto idCardImageReqeustDto) {
		IdCardImageResponseDto response = authService.uploadIdCardImage(idCardImageReqeustDto.getIdCardImage());
		return ApiResponse.success(SuccessCode.UPLOAD_IMAGE_SUCCESS, response);
	}

	@GetMapping("/me")
	public ApiResponse<MemberResponseDto> getMyAccount() {
		Member member = authService.findLoggedInMember();
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

	@DeleteMapping("/withdrawal")
	public ApiResponse<Object> withdrawal() {
		authService.withdrawal();
		return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS, null);
	}

	@PostMapping("/email")
	public ApiResponse<EmailResponseDto> sendCodeMail(@RequestBody @Valid EmailRequestDto emailRequestDto) {
		EmailResponseDto response = authService.sendCodeMail(emailRequestDto);
		return ApiResponse.success(SuccessCode.EMAIL_SEND_SUCCESS, response);
	}

	@PostMapping("/email/certification")
	public ApiResponse<CertificationResponseDto> checkEmailAndCode(
		@RequestBody @Valid CertificationRequestDto certificationRequestDto) {
		CertificationResponseDto response = authService.checkEmailAndCode(certificationRequestDto);
		return ApiResponse.success(SuccessCode.EMAIL_CERTIFICATION_SUCCESS, response);
	}

	@PostMapping("/nickname")
	public ApiResponse<NicknameResponseDto> checkDuplicateNickname(
		@RequestBody @Valid NicknameRequestDto nicknameRequestDto) {
		NicknameResponseDto response = authService.checkDuplicateNickname(nicknameRequestDto);
		return ApiResponse.success(SuccessCode.NICKNAME_CHECK_SUCCESS, response);
	}
}
