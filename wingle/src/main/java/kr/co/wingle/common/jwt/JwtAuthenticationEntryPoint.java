package kr.co.wingle.common.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.dto.ApiResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		setResponse(response, ErrorCode.UNAUTHORIZED_USER);
	}

	private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ApiResponse<Object> apiResponse = ApiResponse.error(code);
		response.getWriter().println(mapper.writeValueAsString(apiResponse));
	}
}
