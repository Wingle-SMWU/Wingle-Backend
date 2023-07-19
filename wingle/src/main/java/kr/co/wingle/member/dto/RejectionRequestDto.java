package kr.co.wingle.member.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RejectionRequestDto {
	@NotNull(message = "사용자 id가 없습니다.")
	private String userId;
	@NotNull(message = "거절 사유가 없습니다.")
	private String reason;
}
