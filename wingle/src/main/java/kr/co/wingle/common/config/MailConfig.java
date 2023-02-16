package kr.co.wingle.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class MailConfig {
	@Value("${spring.mail.username}")
	private String username;
	private final String title = "윙글(Wingle) 이메일 인증코드";
	private final String name = "wingle";
	private final long validTime = 60 * 3L; // 3분
}