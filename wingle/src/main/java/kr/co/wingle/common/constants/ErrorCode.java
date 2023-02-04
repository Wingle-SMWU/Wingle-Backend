package kr.co.wingle.common.constants;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	EXAMPLE_ERROR(BAD_REQUEST, "예시 실패"),
	NO_ID(BAD_REQUEST, "존재하지 않는 id 입니다"),
	ALREADY_DELETED(BAD_REQUEST, "이미 삭제된 값입니다"),
	BAD_FILE_NAME(BAD_REQUEST, "파일 이름이 올바르지 않습니다."),
	BAD_FILE_EXTENSION(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
	EMAIL_BAD_REQUEST(BAD_REQUEST, "이메일 형식이 유효하지 않습니다."),
	DUPLICATE_EMAIL(BAD_REQUEST, "이미 가입된 유저입니다."),

	/* 401 UNAUTHORIZED: 인증 실패 */
	UNAUTHORIZED_USER(UNAUTHORIZED, "인증된 사용자가 아닙니다."),
	WRONG_TYPE_TOKEN(UNAUTHORIZED, "잘못된 JWT 서명을 가진 토큰입니다."),
	EXPIRED_TOKEN(UNAUTHORIZED, "만료된 JWT 토큰입니다."),
	UNSUPPORTED_TOKEN(UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
	WRONG_TOKEN(UNAUTHORIZED, "잘못된 JWT 토큰입니다."),
	UNKNOWN_ERROR(UNAUTHORIZED, "알 수 없는 요청 인증 에러입니다. 헤더에 토큰을 넣어 보냈는지 확인해주세요."),
	ACCESS_DENIED(UNAUTHORIZED, "접근이 거절되었습니다."),

	/* 404 NOT_FOUND: 리소스를 찾을 수 없음 */
	USER_NOT_FOUND(NOT_FOUND, "유저 정보가 없습니다."),

	FILE_UPLOAD_FAIL(INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");

	private final HttpStatus status;
	private final String message;
}
