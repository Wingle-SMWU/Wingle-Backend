package kr.co.wingle.message.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomResponseDto {
	private Long roomId;

	public static RoomResponseDto from(Long roomId) {
		return new RoomResponseDto(roomId);
	}
}