package kr.co.wingle.community.article;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ArticleResponseDto {
	private Long articleId;
	private String userId;
	private String userNickname;
	private String userImage;
	private String userNation;
	private String userSchoolName;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private Long forumId;
	private String content;
	private List<String> images;
	private Boolean isMine;
	private int likeCount;
	private int commentCount;
}
