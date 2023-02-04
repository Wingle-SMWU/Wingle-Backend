package kr.co.wingle.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.wingle.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(value = {CustomException.class})
	protected ApiResponse<Object> handleCustomException(CustomException e, HttpServletRequest request) {
		log.info(String.format("[%s Error] : %s %s", e.getErrorCode().getStatus(), request.getMethod(),
			request.getRequestURI()));
		return ApiResponse.error(e.getErrorCode());
	}

	// @RequestBody valid 에러
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	protected ApiResponse<Object> handleMethodArgNotValidException(MethodArgumentNotValidException e,
		HttpServletRequest request) {
		String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		log.info(String.format("[400 Error] : %s %s %s", request.getMethod(), request.getRequestURI(), message));
		return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
	}

	// @ModelAttribute valid 에러
	@ExceptionHandler(value = {BindException.class})
	protected ApiResponse<Object> handleMethodArgNotValidException(BindException e, HttpServletRequest request) {
		String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		log.info(String.format("[400 Error] : %s %s %s", request.getMethod(), request.getRequestURI(), message));
		return ApiResponse.error(HttpStatus.BAD_REQUEST, message);
	}
}
