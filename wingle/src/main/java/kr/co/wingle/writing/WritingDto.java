package kr.co.wingle.writing;

import java.time.LocalDateTime;

import kr.co.wingle.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WritingDto {
	private Long id;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private Member member;
	private String content;

	private WritingDto(Member member, String content) {
		this.member = member;
		this.content = content;
	}

	public static WritingDto fromEntity(Writing writing) {

		return new WritingDto(writing.getId(), writing.getCreatedTime(), writing.getUpdatedTime(), writing.getMember(),
			writing.getContent());
	}

}
