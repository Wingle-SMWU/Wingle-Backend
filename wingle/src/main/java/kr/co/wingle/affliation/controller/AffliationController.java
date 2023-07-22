package kr.co.wingle.affliation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.affliation.dto.SchoolResponseDto;
import kr.co.wingle.affliation.service.SchoolService;
import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/affliation")
@RequiredArgsConstructor
public class AffliationController {
	private final SchoolService schoolService;

	@GetMapping("/schools")
	ApiResponse<List<SchoolResponseDto>> getSchools() {
		List<SchoolResponseDto> response = schoolService.getSchools();
		return ApiResponse.success(SuccessCode.SCHOOL_GET_SUCCESS, response);
	}
}
