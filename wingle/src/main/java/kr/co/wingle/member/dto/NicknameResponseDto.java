package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameResponseDto {
	private boolean isAvailable;

	public static NicknameResponseDto of(boolean isAvailable) {
		NicknameResponseDto nicknameResponseDto = new NicknameResponseDto();
		nicknameResponseDto.isAvailable = isAvailable;
		return nicknameResponseDto;
	}
}
