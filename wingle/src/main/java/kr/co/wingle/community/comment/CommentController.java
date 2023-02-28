package kr.co.wingle.community.comment;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/community/")
@Slf4j
public class CommentController {
	private final CommentService commentService;

	@PostMapping("/articles/comments")
	public ApiResponse<CommentResponseDto> create(@ModelAttribute @Valid CommentRequestDto request) {
		CommentResponseDto response = commentService.create(request);
		return ApiResponse.success(SuccessCode.COMMENT_CREATE_SUCCESS, response);
	}
}
