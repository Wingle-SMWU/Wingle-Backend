package kr.co.wingle.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EXAMPLE_ERROR(BAD_REQUEST, "예시 실패");

    private final HttpStatus status;
    private final String message;
}
