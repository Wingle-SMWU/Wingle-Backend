package kr.co.wingle.message.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		Member member = authService.findAcceptedLoggedInMember();
		Room room = roomService.getRoomById(messageRequestDto.getRoomId());
		// 해당 쪽지방에 있는지 검사
		roomService.isValidRoomMember(member.getId(), room.getId());

		Message message = Message.of(member, messageRequestDto.getContent(), room);
		messageRepository.save(message);
		return messageMapper.toResponseDto(message);
	}

	@Transactional(readOnly = true)
	public List<MessageResponseDto> getListByRoom(Long roomId, int page, int size) {
		Member member = authService.findAcceptedLoggedInMember();
		roomService.isValidRoomMember(member.getId(), roomId);

		Pageable pageable = PageRequest.of(page, size);
		List<Message> pages = messageRepository.findByRoomIdAndIsDeletedOrderByCreatedTimeDesc(roomId, false, pageable);

		if (pages.isEmpty()) {
			return new ArrayList<MessageResponseDto>();
		}

		List<MessageResponseDto> result = pages.stream()
			.map(messageMapper::toResponseDto).collect(Collectors.toList());
		Collections.sort(result, Collections.reverseOrder());
		return result;
	}

}