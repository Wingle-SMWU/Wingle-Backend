package kr.co.wingle.member.dto;

import javax.validation.constraints.NotNull;

public class MemoRequestDto {
	@NotNull(message = "사용자 id가 없습니다.")
	private Long userId;
	@NotNull(message = "메모가 없습니다.")
	private String memo;
}
