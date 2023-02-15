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
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.MemberResponseDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.TokenRequestDto;

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

	@Test
	void 로그인() throws Exception {
		LoginRequestDto requestDto = LoginRequestDto.of(MemberTemplate.EMAIL, MemberTemplate.PASSWORD);
		TokenDto tokenDto = TokenDto.of("accessToken", "refreshToken");
		given(authService.login(any(LoginRequestDto.class)))
			.willReturn(tokenDto);

		MockHttpServletRequestBuilder builder = post("/api/v1/auth/login")
			.content(mapper.writeValueAsString(requestDto))
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.LOGIN_SUCCESS, tokenDto))
			))
			.andDo(print());
	}

	@Test
	void 계정_조회() throws Exception {
		Member member = MemberTemplate.makeTestMember();
		MemberResponseDto responseDto = MemberResponseDto.from(member);

		given(authService.findMember())
			.willReturn(member);

		MockHttpServletRequestBuilder builder = get("/api/v1/auth/me")
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.ACCOUNT_READ_SUCCESS, responseDto))
			))
			.andDo(print());
	}

	@Test
	void 토큰_재발급() throws Exception {
		TokenRequestDto requestDto = TokenRequestDto.of("refreshToken");
		TokenDto tokenDto = TokenDto.of("accessToken", "refreshToken");
		given(authService.reissue(any(TokenRequestDto.class)))
			.willReturn(tokenDto);

		MockHttpServletRequestBuilder builder = post("/api/v1/auth/refresh")
			.content(mapper.writeValueAsString(requestDto))
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.TOKEN_REISSUE_SUCCESS, tokenDto))
			))
			.andDo(print());
	}
}
