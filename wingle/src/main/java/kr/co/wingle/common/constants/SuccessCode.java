package kr.co.wingle.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    EXAMPLE_SUCCESS(OK, "예시 성공"),
    SIGNUP_SUCCESS(OK, "회원가입 성공"),
    LOGIN_SUCCESS(OK, "로그인 성공"),
    LOGOUT_SUCCESS(OK, "로그아웃 성공"),
    ACCOUNT_READ_SUCCESS(OK, "계정 조회 성공"),
    TOKEN_REISSUE_SUCCESS(OK, "토큰 재발급 성공");

    private final HttpStatus status;
    private final String message;
}
