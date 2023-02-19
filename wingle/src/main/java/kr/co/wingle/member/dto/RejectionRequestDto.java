package kr.co.wingle.member.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RejectionRequestDto {
	@NotNull(message = "사용자 id가 없습니다.")
	private Long userId;
	@Size(max = 40)
	private String reason;
}
