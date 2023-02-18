package kr.co.wingle.member.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcceptanceRequestDto {
	@NotNull(message = "사용자 id가 없습니다.")
	private Long userId;
}
