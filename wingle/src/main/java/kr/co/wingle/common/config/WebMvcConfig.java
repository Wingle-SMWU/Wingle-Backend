package kr.co.wingle.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final String host = "https://wingle.kr";
	private final String admin = "https://adm-wingle.netlify.app";

	private final String[] web = new String[] {"https://wingle-kder.vercel.app",
		"https://wingle-national.netlify.app", "https://dev-wingle.netlify.app"};
	private final String localhost = "http://localhost:";
	private final int allowedMinPort = 3000;
	private final int allowedMaxPort = 3010;
	private List<String> allowedOrigins = new ArrayList<>();

	// CORS
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 허용할 origin 목록
		int allowedPort = allowedMinPort;
		allowedOrigins.add(host);
		allowedOrigins.add(admin);
		allowedOrigins.addAll(Arrays.asList(web));
		while (allowedPort <= allowedMaxPort) {
			allowedOrigins.add(localhost + allowedPort);
			allowedPort += 1;
		}

		registry.addMapping("/**")
			.allowedOrigins(allowedOrigins.toArray(new String[allowedOrigins.size()]))
			.allowedMethods(
				HttpMethod.GET.name(),
				HttpMethod.HEAD.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.DELETE.name())
			.maxAge(3000); // pre-flight 리퀘스트를 캐싱
	}
}
