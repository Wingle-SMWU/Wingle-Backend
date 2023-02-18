package kr.co.wingle.member;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.AcceptanceRequestDto;
import kr.co.wingle.member.dto.PermissionResponseDto;
import kr.co.wingle.member.dto.RejectionRequestDto;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final AuthService authService;

	@PostMapping("/permission/acceptance")
	public ApiResponse<PermissionResponseDto> accept(@RequestBody @Valid AcceptanceRequestDto acceptanceRequestDto) {
		//checkAdminAccount();
		PermissionResponseDto response = authService.sendAcceptanceMail(acceptanceRequestDto);
		return ApiResponse.success(SuccessCode.ACCEPTANCE_SUCCESS, response);
	}

	@PostMapping("/permission/rejection")
	public ApiResponse<PermissionResponseDto> reject(@RequestBody @Valid RejectionRequestDto rejectionRequestDto) {
		//checkAdminAccount();
		PermissionResponseDto response = authService.sendRejectionMail(rejectionRequestDto);
		return ApiResponse.success(SuccessCode.REJECTION_SUCCESS, response);
	}
}
