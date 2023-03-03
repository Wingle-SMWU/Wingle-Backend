package kr.co.wingle.message.mapper;

import org.springframework.stereotype.Component;

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

	public MessageResponseDto toResponseDto(Message message) {
		boolean isMine = writingUtil.isMine(message);
		Profile profile = profileService.getProfileByMemberId(message.getMember().getId());
		return MessageResponseDto.of(message.getId(), profile.getNickname(), message.getContent(),
			message.getCreatedTime(), isMine);
	}
}
