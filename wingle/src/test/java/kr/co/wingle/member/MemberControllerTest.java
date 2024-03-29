package kr.co.wingle.member;

import static kr.co.wingle.member.MemberTemplate.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.SignupListResponseDto;
import kr.co.wingle.member.dto.WaitingUserResponseDto;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.member.service.MemberService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WithMockUser(value = EMAIL, password = PASSWORD)
@AutoConfigureMockMvc
class MemberControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@MockBean
	private AuthService authService;

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setup(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@Test
	void 수락_대기_목록_조회() throws Exception {
		Member adminMember = makeTestAdminMember();
		given(authService.findLoggedInMember())
			.willReturn(adminMember);
		List<SignupListResponseDto> response = new ArrayList<>();
		given(memberService.getWaitingList(anyInt()))
			.willReturn(response);

		MockHttpServletRequestBuilder builder = get("/api/v1/admin/list/waiting/0")
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.WAITING_LIST_READ_SUCCESS, response))
			))
			.andDo(print());
	}

	@Test
	void 수락_대기_사용자_조회() throws Exception {
		Member adminMember = makeTestAdminMember();
		given(authService.findLoggedInMember())
			.willReturn(adminMember);
		WaitingUserResponseDto response = WaitingUserResponseDto.from(makeTestMember(), "KR");
		given(memberService.getWaitingUserInfo(anyLong()))
			.willReturn(response);

		MockHttpServletRequestBuilder builder = get("/api/v1/admin/waiting/{userId}", 1L)
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.WAITING_USER_READ_SUCCESS, response))
			))
			.andDo(print());
	}

	@Test
	void 수락_거절_목록_조회() throws Exception {
		Member adminMember = makeTestAdminMember();
		given(authService.findLoggedInMember())
			.willReturn(adminMember);
		List<SignupListResponseDto> response = new ArrayList<>();
		given(memberService.getRejectionList(anyInt()))
			.willReturn(response);

		MockHttpServletRequestBuilder builder = get("/api/v1/admin/list/rejection/0")
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.REJECTION_LIST_READ_SUCCESS, response))
			))
			.andDo(print());
	}

	@Test
	void 수락_완료_목록_조회() throws Exception {
		Member adminMember = makeTestAdminMember();
		given(authService.findLoggedInMember())
			.willReturn(adminMember);
		List<SignupListResponseDto> response = new ArrayList<>();
		given(memberService.getAcceptanceList(anyInt()))
			.willReturn(response);

		MockHttpServletRequestBuilder builder = get("/api/v1/admin/list/acceptance/0")
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.ACCEPTANCE_LIST_READ_SUCCESS, response))
			))
			.andDo(print());
	}
}