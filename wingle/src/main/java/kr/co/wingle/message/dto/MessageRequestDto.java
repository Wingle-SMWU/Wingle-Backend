package kr.co.wingle.message.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequestDto {
	@Positive(message = "id는 양수만 가능합니다.")
	@NotNull(message = "쪽지방 id가 없습니다.")
	private Long roomId;

	@NotBlank(message = "내용이 없습니다")
	@Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하만 가능합니다.")
	private String content;
}
