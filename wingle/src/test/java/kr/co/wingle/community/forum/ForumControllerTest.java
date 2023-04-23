package kr.co.wingle.community.forum;

import static kr.co.wingle.util.RestDocsConfig.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.util.RestDocsTestSupport;

public class ForumControllerTest extends RestDocsTestSupport {
	@MockBean
	private ForumService forumService;

	protected FieldDescriptor[] forumData() {
		return new FieldDescriptor[] {
			fieldWithPath("id").type(JsonFieldType.NUMBER)
				.description("게시판 id")
				.attributes(field("constraints", "양수 정수")),
			fieldWithPath("name").type(JsonFieldType.STRING)
				.description("게시판 이름")
		};
	}

	@Test
	void 게시판_목록_조회() throws Exception {
		List<ForumResponseDto> responseDto = new ArrayList<ForumResponseDto>();
		responseDto.add(ForumResponseDto.of(1L, "공지"));
		responseDto.add(ForumResponseDto.of(2L, "자유"));

		given(forumService.findAll())
			.willReturn(responseDto);

		MockHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.get("/api/v1/community/forums")
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.GET_SUCCESS, responseDto))
			))
			.andDo(getResponseFields(forumData()));
	}
}
