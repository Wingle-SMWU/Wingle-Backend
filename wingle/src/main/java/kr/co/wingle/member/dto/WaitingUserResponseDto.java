package kr.co.wingle.member.dto;

import java.time.LocalDateTime;

import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingUserResponseDto {
	private Long userId;
	private String name;
	private LocalDateTime createdTime;
	private String idCardImage;
	private String reason;
	private String memo;
	private String nation;

	public static WaitingUserResponseDto from(Member member, String nation) {
		WaitingUserResponseDto waitingUserResponseDto = new WaitingUserResponseDto();
		waitingUserResponseDto.userId = member.getId();
		waitingUserResponseDto.name = member.getName();
		waitingUserResponseDto.createdTime = member.getCreatedTime();
		waitingUserResponseDto.idCardImage = member.getIdCardImageUrl();
		waitingUserResponseDto.reason = member.getRejectionReason();
		waitingUserResponseDto.memo = member.getMemo();
		waitingUserResponseDto.nation = nation;
		return waitingUserResponseDto;
	}
}
