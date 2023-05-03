package kr.co.wingle.common.constants;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	/* 400 BAD_REQUEST: 잘못된 요청 구문 */
	// 일반
	EXAMPLE_ERROR(BAD_REQUEST, "예시 실패"),
	NO_ID(BAD_REQUEST, "존재하지 않는 id 입니다"),
	ALREADY_DELETED(BAD_REQUEST, "이미 삭제된 값입니다"),
	BAD_PARAMETER(BAD_REQUEST, "요청 파라미터가 잘못되었습니다."),
	BAD_PARAMETER_TYPE(BAD_REQUEST, "지원하지 않는 파라미터 형식입니다."),
	// 커뮤니티
	NO_FORUM(BAD_REQUEST, "존재하지 않는 게시판입니다"),
	NO_ARTICLE_ID(BAD_REQUEST, "존재하지 않는 게시물 id 입니다"),
	// 쪽지
	NO_ROOM(BAD_REQUEST, "쪽지방이 없습니다"),
	// 파일
	BAD_FILE_NAME(BAD_REQUEST, "파일 이름이 올바르지 않습니다."),
	BAD_FILE_EXTENSION(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
	// 인증
	INCONSISTENT_CODE(BAD_REQUEST, "인증정보가 일치하지 않습니다."),
	DUPLICATE_EMAIL(BAD_REQUEST, "이미 가입된 유저입니다."),
	ALREADY_ACCEPTANCE(BAD_REQUEST, "이미 가입 승인한 유저입니다."),
	ALREADY_DENY(BAD_REQUEST, "이미 가입 거절한 유저입니다."),
	ALREADY_WITHDROWN(BAD_REQUEST, "이미 탈퇴한 유저입니다."),
	// 메일
	EMAIL_BAD_REQUEST(BAD_REQUEST, "이메일 형식이 유효하지 않습니다."),
	EMAIL_SEND_FAIL(BAD_REQUEST, "이메일을 전송할 수 없습니다."),
	NO_EMAIL_CODE(BAD_REQUEST, "해당 이메일에 유효한 인증정보가 없습니다."),
	// 프로필
	DUPLICATE_NICKNAME(BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
	NO_PROFILE(BAD_REQUEST, "존재하지 않는 프로필입니다"),

	/* 401 UNAUTHORIZED: 인증 실패 */
	UNAUTHORIZED_USER(UNAUTHORIZED, "만료되었거나 잘못된 토큰입니다. 토큰을 확인해주세요."),

	/* 403 FORBIDDEN: 권한 없음 */
	FORBIDDEN_USER(FORBIDDEN, "접근 권한이 없는 유저입니다."),

	/* 404 NOT_FOUND: 리소스를 찾을 수 없음 */
	DATA_NOT_FOUND(NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),
	USER_NOT_FOUND(NOT_FOUND, "유저를 찾을 수 없습니다."),
	TOKEN_NOT_FOUND(NOT_FOUND, "다시 로그인해주세요."),
	PROFILE_NOT_FOUND(NOT_FOUND, "유저의 프로필 정보가 존재하지 않습니다."),

	/* 500 INTERNAL_SERVER_ERROR : 서버 오류 */
	FILE_UPLOAD_FAIL(INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
	SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류로 인해 응답을 제공할 수 없습니다.");

	private final HttpStatus status;
	private final String message;
}
