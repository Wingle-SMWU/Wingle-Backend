package kr.co.wingle.message.mapper;

import org.springframework.stereotype.Component;

import kr.co.wingle.message.dto.RoomResponseDto;
import kr.co.wingle.message.entity.RoomMember;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomMapper {
	public RoomResponseDto roomMembertoResponseDto(RoomMember roomMember) {
		return RoomResponseDto.from(roomMember.getRoom().getId());
	}
}
