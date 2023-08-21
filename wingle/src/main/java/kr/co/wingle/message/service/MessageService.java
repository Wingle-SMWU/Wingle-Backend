package kr.co.wingle.message.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.message.dto.MessageRequestDto;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.dto.MessageResponseWithRecipentDto;
import kr.co.wingle.message.dto.RoomMemberDto;
import kr.co.wingle.message.entity.Message;
import kr.co.wingle.message.entity.Room;
import kr.co.wingle.message.entity.RoomMember;
import kr.co.wingle.message.mapper.MessageMapper;
import kr.co.wingle.message.repository.MessageRepository;
import kr.co.wingle.message.repository.RoomMemberRepository;
import kr.co.wingle.profile.ProfileService;
import kr.co.wingle.writing.WritingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService extends WritingService {
	private final MessageRepository messageRepository;
	private final MessageMapper messageMapper;
	private final AuthService authService;
	private final RoomService roomService;
	private final RoomMemberRepository roomMemberRepository;

	private final ProfileService profileService;

	@Transactional
	public MessageResponseDto send(MessageRequestDto messageRequestDto) {
		Member member = authService.findAcceptedLoggedInMember();
		Room room = roomService.getRoomById(messageRequestDto.getRoomId());
		// 해당 쪽지방에 있는지 검사
		roomService.isValidRoomMember(member.getId(), room.getId());

		Message message = Message.of(member, messageRequestDto.getContent(), room);
		messageRepository.save(message);

		// 해당 쪽지방에 있는 모든 유저 찾기
		List<RoomMember> members = roomMemberRepository.findAllByRoomIdAndIsDeleted(room.getId(), false);

		// 본인을 제외한 유저 안읽은 메시지 수 증가
		for (RoomMember rm : members) {
			if (rm.getMember().getId() == member.getId())
				continue;
			rm.setUnreadMessageCount(rm.getUnreadMessageCount() + 1);
		}

		return messageMapper.toResponseDto(message);
	}

	@Transactional(readOnly = true)
	public MessageResponseWithRecipentDto getListByRoom(Long roomId, int page, int size) {
		Member member = authService.findAcceptedLoggedInMember();
		roomService.isValidRoomMember(member.getId(), roomId);
		RoomMemberDto recipient = roomService.getRecipient(roomId, member.getId());

		Pageable pageable = PageRequest.of(page, size);
		List<Message> pages = messageRepository.findByRoomIdAndIsDeletedOrderByCreatedTimeDesc(roomId, false, pageable);

		if (pages.isEmpty()) {
			return MessageResponseWithRecipentDto.of();
		}

		//  내가 쪽지 읽으면 안읽은 쪽지 수 초기화
		if (page == 0) {
			RoomMember roomMember = roomMemberRepository
				.findByRoomIdAndMemberIdAndIsDeleted(roomId, member.getId(), false)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NO_ROOM_MEMBER));
			roomMember.setUnreadMessageCount(0);
		}

		List<MessageResponseDto> messages = pages.stream()
			.map(messageMapper::toResponseDto).collect(Collectors.toList());
		Collections.sort(messages, Collections.reverseOrder());

		return MessageResponseWithRecipentDto.of(
			AES256Util.encrypt(recipient.getMemberId().toString()),
			recipient.isDeleted() ? "" : profileService.getProfile(recipient.getMemberId()).getImage(),
			recipient.isDeleted() ? "(알수없음)" : recipient.getSchoolName(),
			messages
		);
	}

}