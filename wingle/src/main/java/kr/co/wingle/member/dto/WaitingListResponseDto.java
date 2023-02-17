package kr.co.wingle.member.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingListResponseDto {
	private Long userId;
	private LocalDateTime createdTime;
	private String name;
	private String nation;

	public static WaitingListResponseDto of(Long userId, LocalDateTime createdTime, String name, String nation) {
		WaitingListResponseDto waitingListResponseDto = new WaitingListResponseDto();
		waitingListResponseDto.userId = userId;
		waitingListResponseDto.createdTime = createdTime;
		waitingListResponseDto.name = name;
		waitingListResponseDto.nation = nation;
		return waitingListResponseDto;
	}
}
