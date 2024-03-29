package kr.co.wingle.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	// Custom Bad Request Error
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	protected ApiResponse<Object> handleBadRequestException(BadRequestException exception, HttpServletRequest request) {
		logInfo(request, exception.getCode().getStatus(), exception.getCode().getMessage());
		return ApiResponse.error(exception.getCode());
	}

	// Custom Unauthorized Error
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	protected ApiResponse<Object> handleUnauthorizedException(UnauthorizedException exception,
		HttpServletRequest request) {
		logInfo(request, exception.getCode().getStatus(), exception.getCode().getMessage());
		return ApiResponse.error(exception.getCode());
	}

	// Custom Internal Server Error
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InternalServerErrorException.class)
	protected ApiResponse<Object> handleInternalServerErrorException(InternalServerErrorException exception,
		HttpServletRequest request) {
		logInfo(request, exception.getCode().getStatus(), exception.getCode().getMessage());
		return ApiResponse.error(exception.getCode());
	}

	// @RequestBody valid 에러
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse<Object> handleMethodArgNotValidException(MethodArgumentNotValidException exception,
		HttpServletRequest request) {
		String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		logInfo(request, HttpStatus.BAD_REQUEST, message);
		return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
	}

	// @ModelAttribute valid 에러
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	protected ApiResponse<Object> handleMethodArgNotValidException(BindException exception,
		HttpServletRequest request) {
		String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		logInfo(request, HttpStatus.BAD_REQUEST, message);
		return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(BadCredentialsException.class)
	protected ApiResponse<Object> handleBadCredentialsException(BadCredentialsException exception,
		HttpServletRequest request) {
		logInfo(request, HttpStatus.NOT_FOUND, exception.getMessage());
		return ApiResponse.error(ErrorCode.USER_NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ApiResponse<Object> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
		logInfo(request, exception.getCode().getStatus(), exception.getMessage());
		return ApiResponse.error(exception.getCode());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateException.class)
	public ApiResponse<Object> handleDuplicationException(DuplicateException exception, HttpServletRequest request) {
		logInfo(request, exception.getCode().getStatus(), exception.getMessage());
		return ApiResponse.error(exception.getCode());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	public ApiResponse<Object> handlerForbiddenException(ForbiddenException exception, HttpServletRequest request) {
		logInfo(request, exception.getCode().getStatus(), exception.getMessage());
		return ApiResponse.error(exception.getCode());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ApiResponse<Object> unhandledExceptionHandler(Exception exception, HttpServletRequest request) {
		logWarn(request, exception);
		return ApiResponse.error(ErrorCode.SERVER_ERROR);
	}

	private void logInfo(HttpServletRequest request, HttpStatus status, String message) {
		log.info("{} {} : {} - {} (traceId: {})",
			request.getMethod(), request.getRequestURI(), status, message, getTraceId());
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
