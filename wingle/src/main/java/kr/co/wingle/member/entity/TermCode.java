package kr.co.wingle.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermCode {
	TERMS_OF_USE("서비스 이용약관", true, 0L),
	TERMS_OF_PERSONAL_INFORMATION("개인정보 수집 및 이용 동의", true, 0L),
	TERMS_OF_PROMOTION("이벤트, 프로모션 알림 메일 수신", false, 0L);

	private final String name;
	private final boolean necessity;
	private final Long code; //부모코드
}
