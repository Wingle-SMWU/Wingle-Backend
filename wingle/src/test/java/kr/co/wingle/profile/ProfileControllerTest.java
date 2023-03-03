package kr.co.wingle.profile;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.member.MemberTemplate;
import kr.co.wingle.profile.dto.InterestsRequestDto;
import kr.co.wingle.profile.dto.InterestsResponseDto;
import kr.co.wingle.profile.dto.IntroductionRequestDto;
import kr.co.wingle.profile.dto.IntroductionResponseDto;
import kr.co.wingle.profile.dto.LanguagesRequestDto;
import kr.co.wingle.profile.dto.LanguagesResponseDto;
import kr.co.wingle.profile.dto.ProfileRequestDto;
import kr.co.wingle.profile.dto.ProfileResponseDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ProfileControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private ProfileService profileService;

	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setup(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@Test
	void 프로필_저장() throws Exception {
		ProfileResponseDto responseDto = ProfileResponseDto.of("imageUrl", "nickname");

		given(profileService.saveProfile(any(ProfileRequestDto.class)))
			.willReturn(responseDto);

		MockHttpServletRequestBuilder builder = multipart("/api/v1/profile")
			.file(MemberTemplate.getProfileImage())
			.param("isImageDelete", String.valueOf(false))
			.param("nickname", "nickname")
			.contentType(MediaType.MULTIPART_FORM_DATA);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.PROFILE_SAVE_SUCCESS, responseDto))
			))
			.andDo(print());
	}

	@Test
	void 사용_가능_언어_저장() throws Exception {
		List<String> languages = new ArrayList<>();
		languages.add("KR");
		LanguagesRequestDto requestDto = new LanguagesRequestDto(languages);
		LanguagesResponseDto responseDto = LanguagesResponseDto.of(new ArrayList<>());

		given(profileService.saveLanguages(any(LanguagesRequestDto.class)))
			.willReturn(responseDto);

		MockHttpServletRequestBuilder builder = post("/api/v1/profile/languages")
			.content(mapper.writeValueAsString(requestDto))
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.LANGUAGES_SAVE_SUCCESS, responseDto))
			))
			.andDo(print());
	}

	@Test
	void 자기소개_저장() throws Exception {
		String introduction = "자기소개";
		IntroductionRequestDto requestDto = new IntroductionRequestDto(introduction);
		IntroductionResponseDto responseDto = IntroductionResponseDto.of(introduction);
		given(profileService.saveIntroduction(any(IntroductionRequestDto.class)))
			.willReturn(responseDto);

		MockHttpServletRequestBuilder builder = post("/api/v1/profile/introduction")
			.content(mapper.writeValueAsString(requestDto))
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.INTRODUCTION_SAVE_SUCCESS, responseDto))
			))
			.andDo(print());
	}

	@Test
	void 관심사_저장() throws Exception {
		InterestsRequestDto requestDto = new InterestsRequestDto(new ArrayList<>());
		InterestsResponseDto responseDto = InterestsResponseDto.of(null);

		given(profileService.saveInterests(any(InterestsRequestDto.class)))
			.willReturn(responseDto);

		MockHttpServletRequestBuilder builder = post("/api/v1/profile/interests")
			.content(mapper.writeValueAsString(requestDto))
			.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().string(
				mapper.writeValueAsString(ApiResponse.success(SuccessCode.INTERESTS_SAVE_SUCCESS, responseDto))
			))
			.andDo(print());
	}

}