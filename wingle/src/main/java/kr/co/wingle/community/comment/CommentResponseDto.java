package kr.co.wingle.community.comment;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentResponseDto {
	private Long id;
	private String userId;
	private String userNickname;
	private String userImage;
	private String userNation;
	private String userSchoolName;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private String content;
	private Boolean isMine;
}
