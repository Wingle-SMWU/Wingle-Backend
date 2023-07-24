package kr.co.wingle.member.dto;

import java.time.LocalDateTime;

import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupListResponseDto {
	private String userId;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private String name;
	private String nation;
	private String schoolName;
	private String permission;

	public static SignupListResponseDto from(Member member, String nation) {
		SignupListResponseDto signupListResponseDto = new SignupListResponseDto();
		signupListResponseDto.userId = AES256Util.encrypt(member.getId().toString());
		signupListResponseDto.createdTime = member.getCreatedTime();
		signupListResponseDto.updatedTime = member.getUpdatedTime();
		signupListResponseDto.name = member.getName();
		signupListResponseDto.nation = nation;
		signupListResponseDto.schoolName = member.getSchool().getName();

		switch (member.getPermission()) {
			case 0 -> signupListResponseDto.permission = "DENY";
			case 1 -> signupListResponseDto.permission = "APPROVE";
			default -> signupListResponseDto.permission = "WAIT";
		}

		return signupListResponseDto;
	}
}
