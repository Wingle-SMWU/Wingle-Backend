package kr.co.wingle.common.exception;

import kr.co.wingle.common.constants.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode code;

	public CustomException(ErrorCode code) {
		super(code.getMessage());
		this.code = code;
	}
}
