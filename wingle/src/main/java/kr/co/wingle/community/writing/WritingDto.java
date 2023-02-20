package kr.co.wingle.community.writing;

import java.time.LocalDateTime;

import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WritingDto {
	private long id;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private Member member;
	private String content;
	private boolean isDeleted;

	public static WritingDto fromEntity(Writing writing) {
		return new WritingDto(writing.getId(), writing.getCreatedTime(), writing.getUpdatedTime(), writing.getMember(),
			writing.getContent(), writing.isDeleted());
	}

}
