package kr.co.wingle.community.forum;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/community")
public class ForumController {

	private final ForumService forumService;

	@GetMapping("/forums")
	ApiResponse<List<ForumResponseDto>> getAllForums() {
		List<ForumResponseDto> response = forumService.findAll();
		return ApiResponse.success(SuccessCode.GET_SUCCESS, response);
	}
}
