package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponseDto {
	private Long id;
	private String name;
	private String nickname;

	public static SignupResponseDto of(Long id, String name, String nickname) {
		SignupResponseDto responseDto = new SignupResponseDto();
		responseDto.id = id;
		responseDto.name = name;
		responseDto.nickname = nickname;
		return responseDto;
	}
}
