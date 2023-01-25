package kr.co.wingle.hello;

import lombok.Getter;

@Getter
public class HelloDto {
	Long id;
	String name;

	private HelloDto(String name) {
		this.name = name;
	}

	private HelloDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static HelloDto from(String name) {
		return new HelloDto(name);
	}

	public static HelloDto of(Long id, String name) {
		return new HelloDto(id, name);
	}
}
