package kr.co.wingle.common.constants;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
	EXAMPLE_SUCCESS(OK, "예시 성공"),
	SIGNUP_SUCCESS(OK, "회원가입 성공"),
	LOGIN_SUCCESS(OK, "로그인 성공"),
	LOGOUT_SUCCESS(OK, "로그아웃 성공"),
	ACCOUNT_READ_SUCCESS(OK, "계정 조회 성공"),
	TOKEN_REISSUE_SUCCESS(OK, "토큰 재발급 성공"),
	NICKNAME_CHECK_SUCCESS(OK, "닉네임 확인 성공"),
	EMAIL_SEND_SUCCESS(OK, "이메일 인증코드 전송 성공"),
	EMAIL_CERTIFICATION_SUCCESS(OK, "이메일 인증 성공"),
	ACCEPTANCE_SUCCESS(OK, "회원가입 수락 전송 성공"),
	REJECTION_SUCCESS(OK, "회원가입 거절 전송 성공"),
	WAITING_LIST_READ_SUCCESS(OK, "수락 대기 목록 조회 성공"),
	WAITING_USER_READ_SUCCESS(OK, "수락 대기 사용자 조회 성공"),
	REJECTION_LIST_READ_SUCCESS(OK, "수락 거절 목록 조회 성공"),
	ACCEPTANCE_LIST_READ_SUCCESS(OK, "수락 완료 목록 조회 성공"),
	PROFILE_SAVE_SUCCESS(OK, "프로필 저장 성공"),
	PROFILE_READ_SUCCESS(OK,"프로필 조회 성공"),
	PROFILE_REGISTER_READ_SUCCESS(OK,"프로필 등록 여부 조회 성공"),
	LANGUAGES_SAVE_SUCCESS(OK, "사용 가능 언어 저장 성공"),
	INTRODUCTION_SAVE_SUCCESS(OK, "자기소개 저장 성공"),
	INTERESTS_SAVE_SUCCESS(OK, "관심사 저장 성공");

	private final HttpStatus status;
	private final String message;
	}
