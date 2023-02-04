package kr.co.wingle.common.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LogConfig implements WebMvcConfigurer {
	private final LogTraceIdInterceptor logTraceIdInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logTraceIdInterceptor)
			.addPathPatterns("/**");
	}
}
