package kr.co.wingle.member;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.AcceptanceRequestDto;
import kr.co.wingle.member.dto.AcceptanceResponseDto;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final AuthService authService;

	@GetMapping("")
	public void hello() {
		System.out.println("hello");
	}

	@PostMapping("/permission/acceptance")
	public ApiResponse<AcceptanceResponseDto> accept(@RequestBody @Valid AcceptanceRequestDto acceptanceRequestDto) {
		//checkAdminAccount();
		AcceptanceResponseDto response = authService.accept(acceptanceRequestDto);
		return ApiResponse.success(SuccessCode.ACCEPTANCE_SUCCESS, response);
	}
}
