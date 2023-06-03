package kr.co.wingle.util;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Disabled
@Import(RestDocsConfig.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RestDocsTestSupport extends ControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	@BeforeEach
	void setUp(final WebApplicationContext context,
		final RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(MockMvcRestDocumentation.documentationConfiguration(provider))  // rest docs 설정 주입
			.alwaysDo(MockMvcResultHandlers.print()) // andDo(print()) 코드 포함
			.alwaysDo(restDocs) // pretty 패턴과 문서 디렉토리 명 정해준것 적용
			.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
			.build();
	}

	protected RestDocumentationResultHandler getResponseFields(FieldDescriptor[] data) {
		return restDocs.document(
			responseFields(beneathPath("data") // response의 data 필드 하위 내용 문서화
					.withSubsectionId("data") // response-fields-data.adoc 파일 생성
				, data)
		);
	}

}