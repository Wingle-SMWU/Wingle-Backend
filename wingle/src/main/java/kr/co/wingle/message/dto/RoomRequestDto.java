package kr.co.wingle.message.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRequestDto {
	@Positive(message = "id는 양수만 가능합니다.")
	@NotNull(message = "id가 없습니다.")
	private Long originId;

	@NotNull(message = "쪽지 시작 지점 타입이 없습니다.")
	private String originType;
}
