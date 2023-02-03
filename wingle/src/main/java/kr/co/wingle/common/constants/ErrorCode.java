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
	FILE_UPLOAD_FAIL(INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");

	private final HttpStatus status;
	private final String message;
}
