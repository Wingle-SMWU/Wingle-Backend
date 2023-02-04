package kr.co.wingle.common.exception;

import javax.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(value = {CustomException.class})
	protected ApiResponse<Object> handleCustomException(CustomException exception, HttpServletRequest request) {
		logInfo(request, exception.getErrorCode().getStatus().toString(), exception.getErrorCode().getMessage());
		return ApiResponse.error(exception.getErrorCode());
	}

	// @RequestBody valid 에러
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	protected ApiResponse<Object> handleMethodArgNotValidException(MethodArgumentNotValidException exception,
		HttpServletRequest request) {
		String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		logInfo(request, "400 BAD_REQUEST", message);
		return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
	}

	// @ModelAttribute valid 에러
	@ExceptionHandler(value = {BindException.class})
	protected ApiResponse<Object> handleMethodArgNotValidException(BindException exception,
		HttpServletRequest request) {
		String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		logInfo(request, "400 BAD_REQUEST", message);
		return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse<Object> unhandledExceptionHandler(Exception exception, HttpServletRequest request) {
		logWarn(request, exception);
		return ApiResponse.error(ErrorCode.SERVER_ERROR);
	}

	private void logInfo(HttpServletRequest request, String errorCode, String message) {
		log.info("{} {} : {} - {} (traceId: {})", request.getMethod(), request.getRequestURI(), errorCode,
			message, getTraceId());
	}

	private void logWarn(HttpServletRequest request, Exception exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		exception.printStackTrace(printWriter);
		String stackTrace = stringWriter.toString();

		log.warn("{} {} (traceId: {})\n{}", request.getMethod(), request.getRequestURI(), getTraceId(), stackTrace);
	}

	private String getTraceId() {
		return MDC.get("traceId");
	}
}
