package kr.co.wingle.community.article;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.common.util.StringUtil;
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

	@GetMapping("/{forumId}/articles/{articleId}")
	public ApiResponse<ArticleResponseDto> getOne(@PathVariable String forumId,
		@PathVariable String articleId) {
		return ApiResponse.success(SuccessCode.GET_SUCCESS,
			articleService.getOne(StringUtil.StringToLong(forumId), StringUtil.StringToLong(articleId)));
	}

	@GetMapping("/{forumId}/articles/")
	public ApiResponse<List<ArticleResponseDto>> getList(@PathVariable String forumId,
		@RequestParam String page, @RequestParam String size, @RequestParam(required = false) String my) {
		if (my != null && !my.toLowerCase().equals("true") && !my.toLowerCase().equals("false"))
			return ApiResponse.error(ErrorCode.BAD_PARAMETER);

		boolean getMine = false;
		if (my != null && my.toLowerCase().equals("true"))
			getMine = true;
		
		return ApiResponse.success(SuccessCode.GET_SUCCESS,
			articleService.getList(StringUtil.StringToLong(forumId), StringUtil.StringToInt(page),
				StringUtil.StringToInt(size), getMine));
	}

	@DeleteMapping("/{forumId}/articles/{articleId}")
	public ApiResponse<Long> delete(@PathVariable String forumId,
		@PathVariable String articleId) {

		return ApiResponse.success(SuccessCode.ARTICLE_DELETE_SUCCESS,
			articleService.delete(StringUtil.StringToLong(forumId), StringUtil.StringToLong(articleId)));
	}

}
