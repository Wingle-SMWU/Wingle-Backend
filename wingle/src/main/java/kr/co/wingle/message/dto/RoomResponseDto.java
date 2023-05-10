package kr.co.wingle.message.dto;

import java.time.LocalDateTime;

import kr.co.wingle.profile.entity.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomResponseDto implements Comparable<RoomResponseDto> {
	private Long roomId;
	private String image;
	private String nation;
	private String nickname;
	private String recentChat;
	private LocalDateTime createdTime;

	public static RoomResponseDto from(Long roomId) {
		return RoomResponseDto.builder().roomId(roomId).build();
	}

	public static RoomResponseDto roomPreview(Long roomId, Profile profile, MessageResponseDto message) {
		RoomResponseDto roomResponseDto = RoomResponseDto.builder()
			.roomId(roomId)
			.image(profile.getImageUrl())
			.nation(profile.getNation())
			.nickname(profile.getNickname())
			.build();

		if (message != null) {
			roomResponseDto.setRecentChat(message.getContent());
			roomResponseDto.setCreatedTime(message.getCreatedTime());
		}
		return roomResponseDto;
	}

	@Override
	public int compareTo(RoomResponseDto response) {
		// createdTime이 없으면 정렬하지 않음
		if (this.getCreatedTime() == null)
			return 0;
		if (response.getCreatedTime().isAfter(this.getCreatedTime())) {
			return 1;
		} else if (response.getCreatedTime().isBefore(this.getCreatedTime())) {
			return -1;
		}
		return 0;
	}
}