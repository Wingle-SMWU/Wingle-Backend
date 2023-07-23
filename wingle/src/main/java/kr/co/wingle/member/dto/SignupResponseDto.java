package kr.co.wingle.member.dto;

import kr.co.wingle.common.util.AES256Util;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponseDto {
	private String id;
	private String name;
	private String nickname;

	public static SignupResponseDto of(Long id, String name, String nickname) {
		SignupResponseDto responseDto = new SignupResponseDto();
		responseDto.id = AES256Util.encrypt(id.toString());
		responseDto.name = name;
		responseDto.nickname = nickname;
		return responseDto;
	}
}
