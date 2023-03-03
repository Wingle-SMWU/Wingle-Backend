package kr.co.wingle.message.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.message.dto.MessageRequestDto;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.entity.Message;
import kr.co.wingle.message.entity.Room;
import kr.co.wingle.message.mapper.MessageMapper;
import kr.co.wingle.message.repository.MessageRepository;
import kr.co.wingle.writing.WritingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService extends WritingService {
	private final MessageRepository messageRepository;
	private final MessageMapper messageMapper;
	private final AuthService authService;
	private final RoomService roomService;

	@Transactional
	public MessageResponseDto send(MessageRequestDto messageRequestDto) {
		Member member = authService.findMember();
		Room room = roomService.getRoomById(messageRequestDto.getRoomId());
		Message message = Message.of(member, messageRequestDto.getContent(), room);
		messageRepository.save(message);
		return messageMapper.toResponseDto(message);
	}
}