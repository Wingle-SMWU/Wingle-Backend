package kr.co.wingle.community.article;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.wingle.common.validator.LengthWithoutCR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleEditRequestDto {

	@LengthWithoutCR(min = 1, max = 3000, message = "내용은 1자 이상 3000자 이하만 가능합니다.")
	private String content;

	private List<String> originImages;
	private List<MultipartFile> newImages;

}
