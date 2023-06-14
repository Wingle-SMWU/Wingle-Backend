package kr.co.wingle.community.comment;

import org.springframework.stereotype.Component;

import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.community.util.CommunityUtil;
import kr.co.wingle.community.util.ProcessedPersonalInformation;
import kr.co.wingle.profile.ProfileService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentMapper {
	private final AES256Util aes;
	private final CommunityUtil communityUtil;
	private final ProfileService profileService;

	public CommentResponseDto toResponseDto(Comment comment) {
		if (comment == null)
			throw new NullPointerException();
		ProcessedPersonalInformation processedPersonalInformation = communityUtil.processPersonalInformation(comment);

		CommentResponseDto.CommentResponseDtoBuilder commentResponseDto = CommentResponseDto.builder();

		commentResponseDto.id(comment.getId());
		commentResponseDto.userId(aes.encrypt(processedPersonalInformation.getProcessedMemberId().toString()));
		commentResponseDto.userNickname(processedPersonalInformation.getNickname());
		commentResponseDto.userImage(profileService.getProfileByMemberId(comment.getMember().getId()).getImageUrl());
		commentResponseDto.userNation(profileService.getProfileByMemberId(comment.getMember().getId()).getNation());
		commentResponseDto.createdTime(comment.getCreatedTime());
		commentResponseDto.updatedTime(comment.getUpdatedTime());
		commentResponseDto.content(comment.getContent());
		commentResponseDto.isMine(processedPersonalInformation.isMine());

		return commentResponseDto.build();
	}
}
