package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoResponseDto {
	private String memo;

	public static MemoResponseDto from(String memo) {
		MemoResponseDto dto = new MemoResponseDto();
		dto.memo = memo;
		return dto;
	}
}
