package kr.co.wingle.community.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.util.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
	@Positive(message = "id는 양수만 가능합니다.")
	@NotNull(message = "게시판 id가 없습니다.")
	private Long forumId;

	@Positive(message = "id는 양수만 가능합니다.")
	@NotNull(message = "게시글 id가 없습니다.")
	private Long articleId;

	@Nullable
	private Long originCommentId;

	@NotBlank(message = "내용이 없습니다.")
	private String content;
	
}
