package kr.co.wingle.member;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.member.dto.AcceptanceRequestDto;
import kr.co.wingle.member.dto.MemoRequestDto;
import kr.co.wingle.member.dto.MemoResponseDto;
import kr.co.wingle.member.dto.PermissionResponseDto;
import kr.co.wingle.member.dto.RejectionRequestDto;
import kr.co.wingle.member.dto.SignupListResponseDto;
import kr.co.wingle.member.dto.WaitingUserResponseDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final AuthService authService;

	@GetMapping("/list/waiting/{page}")
	public ApiResponse<List<SignupListResponseDto>> waitingList(@PathVariable int page) {
		checkAdminAccount();
		List<SignupListResponseDto> response = memberService.getWaitingList(page);
		return ApiResponse.success(SuccessCode.WAITING_LIST_READ_SUCCESS, response);
	}

	@GetMapping("/waiting/{userId}")
	public ApiResponse<WaitingUserResponseDto> waitingUser(@PathVariable Long userId) {
		checkAdminAccount();
		WaitingUserResponseDto response = memberService.getWaitingUserInfo(userId);
		return ApiResponse.success(SuccessCode.WAITING_USER_READ_SUCCESS, response);
	}

	@GetMapping("/list/rejection/{page}")
	public ApiResponse<List<SignupListResponseDto>> rejectionList(@PathVariable int page) {
		checkAdminAccount();
		List<SignupListResponseDto> response = memberService.getRejectionList(page);
		return ApiResponse.success(SuccessCode.REJECTION_LIST_READ_SUCCESS, response);
	}

	@GetMapping("/list/acceptance/{page}")
	public ApiResponse<List<SignupListResponseDto>> acceptanceList(@PathVariable int page) {
		checkAdminAccount();
		List<SignupListResponseDto> response = memberService.getAcceptanceList(page);
		return ApiResponse.success(SuccessCode.ACCEPTANCE_LIST_READ_SUCCESS, response);
	}

	@PostMapping("/permission/acceptance")
	public ApiResponse<PermissionResponseDto> accept(@RequestBody @Valid AcceptanceRequestDto acceptanceRequestDto) {
		checkAdminAccount();
		PermissionResponseDto response = authService.sendAcceptanceMail(acceptanceRequestDto);
		return ApiResponse.success(SuccessCode.ACCEPTANCE_SUCCESS, response);
	}

	@PostMapping("/permission/rejection")
	public ApiResponse<PermissionResponseDto> reject(@RequestBody @Valid RejectionRequestDto rejectionRequestDto) {
		checkAdminAccount();
		PermissionResponseDto response = authService.sendRejectionMail(rejectionRequestDto);
		return ApiResponse.success(SuccessCode.REJECTION_SUCCESS, response);
	}

	@PostMapping("/user/memo")
	public ApiResponse<MemoResponseDto> saveMemo(@RequestBody @Valid MemoRequestDto memoRequestDto) {
		checkAdminAccount();
		MemoResponseDto response = memberService.saveMemo(memoRequestDto);
		return ApiResponse.success(SuccessCode.MEMO_SAVE_SUCCESS, response);
	}

	private void checkAdminAccount() {
		Member member = authService.findMember();
		if (member.getAuthority() == Authority.ROLE_USER) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_USER);
		}
	}
}
