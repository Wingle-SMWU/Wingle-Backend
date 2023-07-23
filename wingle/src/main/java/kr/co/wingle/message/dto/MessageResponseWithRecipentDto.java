package kr.co.wingle.message.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponseWithRecipentDto {
	private String recipientUserId;
	private String recipientImage;
	private String recipientSchoolName;

	private List<MessageResponseDto> messages;

	public static MessageResponseWithRecipentDto of() {
		return MessageResponseWithRecipentDto.builder()
			.recipientImage(null)
			.messages(null)
			.build();
	}

	public static MessageResponseWithRecipentDto of(String recipientUserId, String recipientImage,
		String recipientSchoolName,
		List<MessageResponseDto> messages) {
		return MessageResponseWithRecipentDto.builder()
			.recipientUserId(recipientUserId)
			.recipientImage(recipientImage)
			.recipientSchoolName(recipientSchoolName)
			.messages(messages)
			.build();
	}
}
