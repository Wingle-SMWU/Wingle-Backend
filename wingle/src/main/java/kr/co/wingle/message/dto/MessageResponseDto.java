package kr.co.wingle.message.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageResponseDto {
	private Long messageId;
	private String nickname;
	private String content;
	private LocalDateTime createdTime;
	private boolean isSender;

	public static MessageResponseDto of(Long messageId, String nickname, String content, LocalDateTime createdTime,
		boolean isSender) {
		return MessageResponseDto.builder()
			.messageId(messageId)
			.nickname(nickname)
			.content(content)
			.createdTime(createdTime)
			.isSender(isSender)
			.build();
	}
}
