package kr.co.wingle.message.dto;

import kr.co.wingle.message.OriginType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomMemberDto {
	private Long roomId;
	private OriginType originType;
	private Long memberId;
	private String name;
	private String email;
	private int permission;
	private String schoolName;

	public static RoomMemberDto of(Long roomId, OriginType originType, Long memberId, String name, String email,
		int permission, String schoolName) {
		return RoomMemberDto.builder()
			.roomId(roomId)
			.originType(originType)
			.memberId(memberId)
			.name(name)
			.email(email)
			.permission(permission)
			.schoolName(schoolName)
			.build();
	}
}