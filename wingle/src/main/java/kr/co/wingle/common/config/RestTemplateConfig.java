package kr.co.wingle.common.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplateBuilder restTemplateBuilder() {
		return new RestTemplateBuilder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return restTemplateBuilder()
			.requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
			.setConnectTimeout(Duration.ofMillis(5000)) // connection-timeout
			.setReadTimeout(Duration.ofMillis(5000)) // read-timeout
			.additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
			.build();
	}
}
