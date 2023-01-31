package kr.co.wingle.hello.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateRequestDto {
	@NotBlank(message = "이름이 없습니다.")
	private String name;
}
