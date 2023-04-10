package kr.co.wingle.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	// CORS 오류 해결
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:3002",
				"https://wingle.kr")
			.maxAge(3000); // pre-flight 리퀘스트를 캐싱
	}
}