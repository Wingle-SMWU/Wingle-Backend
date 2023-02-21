package kr.co.wingle.member.dto;

import java.time.LocalDateTime;

import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminListResponseDto {
	private Long userId;
	private LocalDateTime createdTime;
	private String name;
	private String nation;

	public static AdminListResponseDto from(Member member, String nation) {
		AdminListResponseDto adminListResponseDto = new AdminListResponseDto();
		adminListResponseDto.userId = member.getId();
		adminListResponseDto.createdTime = member.getCreatedTime();
		adminListResponseDto.name = member.getName();
		adminListResponseDto.nation = nation;
		return adminListResponseDto;
	}
}
