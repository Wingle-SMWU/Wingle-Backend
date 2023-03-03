package kr.co.wingle.message.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.message.dto.MessageRequestDto;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.entity.Message;
import kr.co.wingle.message.entity.Room;
import kr.co.wingle.message.mapper.MessageMapper;
import kr.co.wingle.message.repository.MessageRepository;
import kr.co.wingle.message.repository.RoomMemberRepository;
import kr.co.wingle.writing.WritingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService extends WritingService {
	private final MessageRepository messageRepository;
	private final RoomMemberRepository roomMemberRepository;
	private final MessageMapper messageMapper;
	private final AuthService authService;
	private final RoomService roomService;

	@Transactional
	public MessageResponseDto send(MessageRequestDto messageRequestDto) {
		Member member = authService.findMember();
		Room room = roomService.getRoomById(messageRequestDto.getRoomId());
		// 해당 쪽지방에 있는지 검사
		roomMemberRepository.findAllByRoomIdAndMemberIdAndIsDeleted(room.getId(), member.getId(),
				false)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.FORBIDDEN_USER));

		Message message = Message.of(member, messageRequestDto.getContent(), room);
		messageRepository.save(message);
		return messageMapper.toResponseDto(message);
	}
}