package kr.co.wingle.community.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.util.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
	@Positive(message = "id는 양수만 가능합니다.")
	@NotNull(message = "게시판 id가 없습니다.")
	private Long forumId;

	@Positive(message = "id는 양수만 가능합니다.")
	@NotNull(message = "게시글 id가 없습니다.")
	private Long articleId;

	@Nullable
	private Long originCommentId;

	@NotNull(message = "내용이 없습니다.")
	@Size(min = 1, max = 500, message = "내용은 1자 이상 500자 이하만 가능합니다.")
	private String content;
}
