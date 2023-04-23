package kr.co.wingle.util;

import static org.springframework.restdocs.snippet.Attributes.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class RestDocsConfig {

	@Bean
	public RestDocumentationResultHandler write() {
		return MockMvcRestDocumentation.document(
			// 조각이 생성되는 디렉토리 명을 클래스명/메서드 명으로 정함
			"{class-name}/{method-name}",
			// json pretty하게 출력
			Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
			Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
		);
	}

	// 제약조건
	public static final Attribute field(
		final String key,
		final String value) {
		return new Attribute(key, value);
	}
}
