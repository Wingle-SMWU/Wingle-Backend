package kr.co.wingle.community.article;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/community")
public class ArticleController {
	private final ArticleService articleService;

	@PostMapping("/articles")
	public ApiResponse<ArticleResponseDto> create(@ModelAttribute @Valid ArticleRequestDto articleRequestDto) {
		ArticleResponseDto response = articleService.create(articleRequestDto);
		return ApiResponse.success(SuccessCode.ARTICLE_CREATE_SUCCESS, response);
	}

}
