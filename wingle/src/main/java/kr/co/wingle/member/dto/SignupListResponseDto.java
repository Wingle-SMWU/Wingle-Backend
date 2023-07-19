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
	private String name;
	private String nation;

	public static SignupListResponseDto from(Member member, String nation) {
		SignupListResponseDto signupListResponseDto = new SignupListResponseDto();
		signupListResponseDto.userId = AES256Util.encrypt(member.getId().toString());
		signupListResponseDto.createdTime = member.getCreatedTime();
		signupListResponseDto.name = member.getName();
		signupListResponseDto.nation = nation;
		return signupListResponseDto;
	}
}
