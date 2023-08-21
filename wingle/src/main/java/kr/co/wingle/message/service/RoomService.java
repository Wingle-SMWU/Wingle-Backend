package kr.co.wingle.message.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.community.article.ArticleService;
import kr.co.wingle.community.comment.CommentService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;
import kr.co.wingle.message.OriginType;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.dto.RoomMemberDto;
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
		// originType이 profile 뿐이라고 가정한 코드입니다
		// 추후 originType이 추가될 경우 분기 필요
		Long userId = AES256Util.userIdDecrypt(roomRequestDto.getOriginId());

		final int oneToOneRoomSize = 2;
		Member loggedInMember = authService.findAcceptedLoggedInMember();
		OriginType originType = OriginType.from(roomRequestDto.getOriginType());
		Room targetRoom = null;

		// 자기자신과 쪽지 방지
		if (loggedInMember.getId() == userId) {
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
				.filter(x -> x.getMember().getId() == userId).collect(Collectors.toList());
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
	Room createRoom(RoomRequestDto roomRequestDto) {
		Long userId = AES256Util.userIdDecrypt(roomRequestDto.getOriginId());

		Member loggedInMember = authService.findAcceptedLoggedInMember();
		OriginType originType = OriginType.from(roomRequestDto.getOriginType());
		if (!isValidOriginTypeAndId(originType, userId)) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}
		Room room = Room.of(userId, originType);
		roomRepository.save(room);

		if (originType == OriginType.PROFILE) {
			Member targetMember = memberService.findAcceptedMemberByMemberId(userId);
			roomMemberRepository.save(RoomMember.of(room, loggedInMember));
			if (loggedInMember.getId() != userId) {
				roomMemberRepository.save(RoomMember.of(room, targetMember));
			}
		}

		return room;
	}

	@Transactional(readOnly = true)
	boolean isValidOriginTypeAndId(OriginType originType, Long id) {
		Member member = authService.findAcceptedLoggedInMember();
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
		Member member = authService.findAcceptedLoggedInMember();
		Pageable pageable = PageRequest.of(page, size);
		List<RoomMember> pages = roomMemberRepository.findByMemberIdAndIsDeleted(member.getId(), false, pageable);

		List<Room> rooms = pages.stream().map(x -> x.getRoom()).collect(Collectors.toList());
		List<RoomResponseDto> result = new ArrayList<>();
		for (Room room : rooms) {
			Member otherMember = roomMemberRepository.findAllByRoomIdAndIsDeleted(room.getId(), false)
				.stream().filter(x -> x.getMember().getId() != member.getId()).collect(Collectors.toList())
				.get(0).getMember();
			Profile otherProfile = otherMember.isDeleted() ? Profile.createDummyProfile(otherMember) :
				profileService.getProfileByMemberId(otherMember.getId());
			MessageResponseDto recent = null;
			// messageService.getListByRoom 이용하고 싶은데 순환구조 생김
			// List<MessageResponseDto> messages = messageService.getListByRoom(room.getId(), 0, 1);
			List<MessageResponseDto> messages = messageRepository.findByRoomIdAndIsDeletedOrderByCreatedTimeDesc(
					room.getId(), false,
					PageRequest.of(0, 1))
				.stream()
				.map(messageMapper::toResponseDto).collect(Collectors.toList());

			RoomMember roomMember = roomMemberRepository
				.findByRoomIdAndMemberIdAndIsDeleted(room.getId(), member.getId(), false)
				.orElseGet(null);

			if (messages.size() > 0)
				recent = messages.get(0);
			result.add(
				RoomResponseDto.roomPreview(room.getId(), otherProfile, recent,
					otherMember.isDeleted() ? "(알수없음)" : otherMember.getSchool().getName(),
					roomMember != null ? roomMember.getUnreadMessageCount() : 0));
		}

		// 최신메시지순
		Collections.sort(result);

		return result;
	}

	@Transactional
	public RoomMemberDto getRecipient(Long roomId, Long userId) {
		List<RoomMember> members = roomMemberRepository.findAllByRoomIdAndIsDeleted(roomId, false);

		RoomMemberDto dto = null;
		for (RoomMember roomMember : members) {
			if (!Objects.equals(roomMember.getMember().getId(), userId)) {
				Room room = roomMember.getRoom();
				Member member = roomMember.getMember();
				dto = RoomMemberDto.of(room.getId(), room.getOriginType(), member.getId(), member.getName(),
					member.getEmail(),
					member.getPermission(),
					member.getSchool().getName(),
					member.isDeleted());
			}
		}

		return dto;
	}
}
