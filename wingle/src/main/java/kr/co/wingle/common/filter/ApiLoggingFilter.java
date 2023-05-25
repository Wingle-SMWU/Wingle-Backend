package kr.co.wingle.common.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {
	private final List<String> excludeUrl = List.of("/");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		if (excludeUrl.contains(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		//response body를 출력하기 위한 wrapper
		ReadableResponseBodyWrapper responseBodyWrapper = new ReadableResponseBodyWrapper(response);

		if (isMultipartFormData(request)) {
			String formData = parameterMapToString(request.getParameterMap());

			// form-data request log 출력
			logRequest(request, formData);
			filterChain.doFilter(request, responseBodyWrapper);

		} else {
			ReadableRequestBodyWrapper requestBodyWrapper = new ReadableRequestBodyWrapper(request);

			// GET 혹은 application/json request log 출력
			logRequest(request, requestBodyWrapper.getRequestBody());
			filterChain.doFilter(requestBodyWrapper, responseBodyWrapper);
		}

		// response log 출력
		logResponse(response, responseBodyWrapper.getResponseBody());
		response.getOutputStream().write(responseBodyWrapper.getDataStream());
	}

	private boolean isMultipartFormData(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && contentType.startsWith("multipart/form-data");
	}

	private void logRequest(HttpServletRequest request, String requestData) {
		log.info("REQUEST {} {} :: {}", request.getMethod(), request.getRequestURI(), requestData);
	}

	private void logResponse(HttpServletResponse response, String responseData) {
		log.info("RESPONSE :: {} :: {}", response.getStatus(), responseData);
	}

	private static String parameterMapToString(Map<String, String[]> parameterMap) {
		// form-data를 parameterMap 형식으로 받은 뒤 string으로 변환하는 작업
		StringBuilder sb = new StringBuilder();
		sb.append("form-data :: { \n");

		parameterMap.forEach((key, value) -> {
			if (sb.length() > 0) {
				sb.append("\n");
			}

			// value가 string[] 형식이기 때문에 string으로 변환하는 과정
			StringBuilder value_string = new StringBuilder();
			for (int i = 0; i < value.length; i++) {
				value_string.append(value[i]);
			}

			sb.append(String.format("%s=%s",
				key.toString(),
				value_string.toString()
			));
		});

		sb.append("\n}");
		return sb.toString();
	}
}
