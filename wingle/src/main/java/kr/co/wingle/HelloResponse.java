package kr.co.wingle;

import lombok.Getter;

@Getter
public class HelloResponse {
    Long id;
    String name;

    private HelloResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static HelloResponse of(Long id, String name) {
        return new HelloResponse(id, name);
    }
}
