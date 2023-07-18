package kr.co.wingle.message.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.entity.Message;
import kr.co.wingle.profile.ProfileService;
import kr.co.wingle.profile.entity.Profile;
import kr.co.wingle.writing.WritingUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MessageMapper {
	private final ProfileService profileService;
	private final WritingUtil writingUtil;

	private final AuthService authService;

	public MessageResponseDto toResponseDto(Message message) {
		boolean isMine = writingUtil.isMine(message);
		Member member = authService.findAcceptedLoggedInMember();

		Profile profile = profileService.getProfileByMemberId(message.getMember().getId());

		// 메세지 송신자가 나 자신일 경우 닉네임 대신 null 반환
		return MessageResponseDto.of(
			message.getId(),
			!Objects.equals(profile.getMember().getId(), member.getId()) ? profile.getNickname() : null,
			message.getContent(),
			message.getCreatedTime(), isMine);
	}
}
