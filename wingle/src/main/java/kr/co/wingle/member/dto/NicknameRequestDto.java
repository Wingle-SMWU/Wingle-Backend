package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameRequestDto {
	@NotBlank(message = "닉네임이 없습니다.")
	private String nickname;
}
