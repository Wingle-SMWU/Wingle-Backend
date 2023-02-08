package kr.co.wingle.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

	private MockMvc mockMvc;

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
	void 회원가입() throws Exception {
		SignupRequestDto requestDto = MemberTemplate.makeTestSignUpRequestDto();
		MockMultipartFile idCardImage = MemberTemplate.getTestIdCardImage();

		SignupResponseDto responseDto = SignupResponseDto.of(1L, requestDto.getName(), requestDto.getNickname());
		given(authService.signup(any(SignupRequestDto.class)))
			.willReturn(responseDto);

		MockHttpServletRequestBuilder builder = multipart("/api/v1/auth/signup")
			.file(idCardImage)
			.param("email", requestDto.getEmail())
			.param("password", requestDto.getPassword())
			.param("name", requestDto.getName())
			.param("nickname", requestDto.getNickname())
			.param("gender", String.valueOf(requestDto.isGender()))
			.param("nation", requestDto.getNation())
			.param("termsOfUse", String.valueOf(requestDto.isTermsOfUse()))
			.param("collectionOfPersonalInformation", String.valueOf(requestDto.isCollectionOfPersonalInformation()))
			.contentType(MediaType.MULTIPART_FORM_DATA);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.SIGNUP_SUCCESS, responseDto))))
			.andDo(print());
	}
}
