package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RejectionResponseDto {
	private String reason;

	public static RejectionResponseDto from(String reason) {
		RejectionResponseDto dto = new RejectionResponseDto();
		dto.reason = reason;
		return dto;
	}
}
