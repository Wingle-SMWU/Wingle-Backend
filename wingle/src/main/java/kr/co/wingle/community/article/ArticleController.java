package kr.co.wingle.community.article;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/community")
@Slf4j
public class ArticleController {
	private final ArticleService articleService;

	@PostMapping("/articles")
	public ApiResponse<ArticleResponseDto> create(@ModelAttribute @Valid ArticleRequestDto articleRequestDto) {
		ArticleResponseDto response = articleService.create(articleRequestDto);
		return ApiResponse.success(SuccessCode.ARTICLE_CREATE_SUCCESS, response);
	}

	@DeleteMapping("/{forumId}/articles/{articleId}")
	public ApiResponse<Long> delete(@PathVariable String forumId,
		@PathVariable String articleId) {
		Long parsedforumId;
		Long parsedarticleId;

		try {
			parsedforumId = Long.parseLong(forumId);
			parsedarticleId = Long.parseLong(articleId);
		} catch (Exception e) {
			return ApiResponse.error(ErrorCode.BAD_PARAMETER_TYPE);
		}

		return ApiResponse.success(SuccessCode.ARTICLE_DELETE_SUCCESS,
			articleService.delete(parsedforumId, parsedarticleId));
	}

}
