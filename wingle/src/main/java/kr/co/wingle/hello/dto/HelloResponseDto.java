package kr.co.wingle.hello.dto;

import lombok.Getter;

@Getter
public class HelloResponseDto {
	Long id;
	String name;

	private HelloResponseDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static HelloResponseDto of(Long id, String name) {
		return new HelloResponseDto(id, name);
	}
}
