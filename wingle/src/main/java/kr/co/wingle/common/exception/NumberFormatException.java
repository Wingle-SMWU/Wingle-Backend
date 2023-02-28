package kr.co.wingle.common.exception;

import kr.co.wingle.common.constants.ErrorCode;

public class NumberFormatException extends RuntimeException {
	private final ErrorCode code;

	public NumberFormatException(ErrorCode code) {
		super(code.getMessage());
		this.code = code;
	}
}