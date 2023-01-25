package kr.co.wingle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WingleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WingleApplication.class, args);
	}

}
