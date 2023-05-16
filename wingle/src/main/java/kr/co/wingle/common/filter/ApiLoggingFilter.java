package kr.co.wingle.common.filter;

import java.io.IOException;

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
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		ReadableRequestBodyWrapper requestBodyWrapper = new ReadableRequestBodyWrapper(request);
		ReadableResponseBodyWrapper responseBodyWrapper = new ReadableResponseBodyWrapper(response);

		// request log 출력
		log.info("REQUEST {} {} :: {} ", request.getMethod(), request.getRequestURI(),
			requestBodyWrapper.getRequestBody());

		filterChain.doFilter(requestBodyWrapper, responseBodyWrapper);

		log.info("RESPONSE :: {} :: {}", response.getStatus(), responseBodyWrapper.getResponseBody());

		response.getOutputStream().write(responseBodyWrapper.getDataStream());
	}
}
