package kr.co.wingle.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;

public class SecurityUtil {

	private SecurityUtil() {
	}

	public static String getCurrentUserEmail() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getName() == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
		}
		if (authentication.getName().equals("anonymousUser")) {
			throw new CustomException(ErrorCode.ACCESS_DENIED);
		}

		return authentication.getName();
	}
}
