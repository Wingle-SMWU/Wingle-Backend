package kr.co.wingle.hello;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class HelloRequest {
	@NotBlank(message = "이름이 없습니다.")
	private String name;
}
