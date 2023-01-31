package kr.co.wingle.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
	DENY(0),
	APPROVE(1),
	WAIT(2);

	private final int status;

	public int getStatus() {
		return status;
	}
}
