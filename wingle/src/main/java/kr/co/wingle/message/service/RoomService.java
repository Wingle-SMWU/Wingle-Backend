package kr.co.wingle.message.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.community.article.ArticleService;
import kr.co.wingle.community.comment.CommentService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import kr.co.wingle.message.OriginType;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.dto.RoomRequestDto;
import kr.co.wingle.message.dto.RoomResponseDto;
import kr.co.wingle.message.entity.Room;
import kr.co.wingle.message.entity.RoomMember;
import kr.co.wingle.message.mapper.MessageMapper;
import kr.co.wingle.message.repository.MessageRepository;
import kr.co.wingle.message.repository.RoomMemberRepository;
import kr.co.wingle.message.repository.RoomRepository;
import kr.co.wingle.profile.ProfileService;
import kr.co.wingle.profile.entity.Profile;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomMemberRepository roomMemberRepository;
	private final MessageRepository messageRepository;
	private final ArticleService articleService;
	private final CommentService commentService;
	private final ProfileService profileService;
	private final AuthService authService;
	private final MemberService memberService;
	private final MessageMapper messageMapper;

	@Transactional(readOnly = true)
	public Room getRoomById(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ROOM));
	}

	@Transactional
	public RoomResponseDto getRoom(RoomRequestDto roomRequestDto) {
		final int oneToOneRoomSize = 2;
		Member loggedInMember = authService.findMember();
		OriginType originType = OriginType.from(roomRequestDto.getOriginType());
		Room targetRoom = null;

		// 자기자신과 쪽지 방지
		if (loggedInMember.getId() == roomRequestDto.getOriginId()) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}

		// 다른 구현 방법: findAllByMemberIdAndIsDeleted(유저1), findAllByMemberIdAndIsDeleted(유저2) 한다음 교집합

		// 룸멤버에서 내가 소속된 룸 리스트 찾기
		List<RoomMember> roomMembers = roomMemberRepository.findAllByMemberIdAndIsDeleted(loggedInMember.getId(),
			false);
		// 룸만 추출
		List<Room> rooms = roomMembers.stream().map(x -> x.getRoom()).collect(Collectors.toList());
		// 룸에서 type 같은 것만 필터링
		rooms = rooms.stream()
			.filter(x -> x.getOriginType().equals(originType))
			.collect(Collectors.toList());
		// 룸 리스트에서 룸id를 이용해 룸멤버에서 roomRequestDto.originId와 엮인 room 찾기
		for (Room room : rooms) {
			List<RoomMember> found = roomMemberRepository.findAllByRoomIdAndIsDeleted(room.getId(), false);
			if (found.size() != oneToOneRoomSize)
				continue;
			List<RoomMember> checkMember = found.stream()
				.filter(x -> x.getMember().getId() == roomRequestDto.getOriginId()).collect(Collectors.toList());
			if (checkMember.size() != 1)
				continue;
			targetRoom = room;
			break;
		}

		if (targetRoom == null) {
			targetRoom = createRoom(roomRequestDto);
		}

		return RoomResponseDto.from(targetRoom.getId());
	}

	@Transactional
	private Room createRoom(RoomRequestDto roomRequestDto) {
		Member loggedInMember = authService.findMember();
		OriginType originType = OriginType.from(roomRequestDto.getOriginType());
		if (!isValidOriginTypeAndId(originType, roomRequestDto.getOriginId())) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}
		Room room = Room.of(roomRequestDto.getOriginId(), originType);
		roomRepository.save(room);

		if (originType == OriginType.PROFILE) {
			Member targetMember = memberService.findMemberByUserId(roomRequestDto.getOriginId());
			roomMemberRepository.save(RoomMember.of(room, loggedInMember));
			if (loggedInMember.getId() != roomRequestDto.getOriginId()) {
				roomMemberRepository.save(RoomMember.of(room, targetMember));
			}
		}

		return room;
	}

	@Transactional(readOnly = true)
	private boolean isValidOriginTypeAndId(OriginType originType, Long id) {
		Member member = authService.findMember();
		boolean isDeleted = switch (originType) {
			case ARTICLE -> articleService.getArticleById(id).isDeleted();
			case COMMENT -> commentService.getCommentById(id).isDeleted();
			case PROFILE -> profileService.getProfileByMemberId(id).isDeleted();
		};
		return !isDeleted;
	}

	@Transactional(readOnly = true)
	public boolean isValidRoomMember(Long MemberId, Long roomId) {
		// 해당 쪽지방에 있는지 검사
		roomMemberRepository.findByRoomIdAndMemberIdAndIsDeleted(roomId, MemberId,
				false)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.FORBIDDEN_USER));
		return true;
	}

	public List<RoomResponseDto> getMyList(int page, int size) {
		Member member = authService.findMember();
		Pageable pageable = PageRequest.of(page, size);
		List<RoomMember> pages = roomMemberRepository.findByMemberIdAndIsDeleted(member.getId(), false, pageable);

		List<Room> rooms = pages.stream().map(x -> x.getRoom()).collect(Collectors.toList());
		List<RoomResponseDto> result = new ArrayList<>();
		for (Room room : rooms) {
			Member otherMember = roomMemberRepository.findAllByRoomIdAndIsDeleted(room.getId(), false)
				.stream().filter(x -> x.getMember().getId() != member.getId()).collect(Collectors.toList())
				.get(0).getMember();
			Profile otherProfile = profileService.getProfileByMemberId(otherMember.getId());
			MessageResponseDto recent = null;
			// messageService.getListByRoom 이용하고 싶은데 순환구조 생김
			// List<MessageResponseDto> messages = messageService.getListByRoom(room.getId(), 0, 1);
			List<MessageResponseDto> messages = messageRepository.findByRoomIdAndIsDeletedOrderByCreatedTimeDesc(
					room.getId(), false,
					PageRequest.of(0, 1))
				.stream()
				.map(messageMapper::toResponseDto).collect(Collectors.toList());

			if (messages.size() > 0)
				recent = messages.get(0);
			result.add(RoomResponseDto.roomPreview(room.getId(), otherProfile, recent));
		}

		// 최신메시지순
		Collections.sort(result);

		return result;
	}
}
