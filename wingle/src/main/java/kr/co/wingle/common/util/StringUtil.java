package kr.co.wingle.common.util;

import org.springframework.stereotype.Component;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.BadRequestException;

@Component
public class StringUtil {
	public static Long StringToLong(String string) {
		try {
			return Long.parseLong(string);
		} catch (Exception e) {
			throw new BadRequestException(ErrorCode.BAD_PARAMETER_TYPE);
		}
	}

	public static int StringToInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			throw new BadRequestException(ErrorCode.BAD_PARAMETER_TYPE);
		}
	}
}
