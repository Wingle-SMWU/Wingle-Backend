package kr.co.wingle.community.article;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.validator.LengthWithLiteralEscape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleRequestDto {
	@NotNull(message = "게시판 id가 없습니다.")
	private Long forumId;

	@LengthWithLiteralEscape(min = 1, max = 1000, message = "내용은 1자 이상 3000자 이하만 가능합니다.")
	private String content;

	private List<MultipartFile> images;

}
