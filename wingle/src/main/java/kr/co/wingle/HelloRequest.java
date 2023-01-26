package kr.co.wingle;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class HelloRequest {
    @NotBlank(message = "이름이 없습니다.")
    private String name;
}
