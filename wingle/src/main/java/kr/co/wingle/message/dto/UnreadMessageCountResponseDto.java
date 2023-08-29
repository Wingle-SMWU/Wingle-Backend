package kr.co.wingle.message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreadMessageCountResponseDto {
	private int unreadMessageCount;

	private UnreadMessageCountResponseDto(int unreadMessageCount) {
		this.unreadMessageCount = unreadMessageCount;
	}

	public static UnreadMessageCountResponseDto from(int unreadMessageCount) {
		return new UnreadMessageCountResponseDto(unreadMessageCount);
	}
}
