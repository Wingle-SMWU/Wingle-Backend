package kr.co.wingle.member;

import static kr.co.wingle.member.MemberTemplate.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.common.util.RedisUtil;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;

@SpringBootTest
@WithMockUser(value = EMAIL, password = PASSWORD)
class AuthServiceTest {
	@Autowired
	private AuthService authService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RedisUtil redisUtil;

	@AfterEach
	void deleteMember() {
		memberRepository.deleteAll();
	}

	@Test
	void 회원가입() throws Exception {
		//given
		SignupRequestDto requestDto = makeTestSignUpRequestDto();

		//when
		SignupResponseDto response = authService.signup(requestDto);
		Member member = memberRepository.findById(response.getId())
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));

		//then
		assertThat(member.getEmail()).isEqualTo(requestDto.getEmail());
	}

	@Test
	void 회원가입_이메일_중복() throws Exception {
		//given
		Member member = makeTestMember();
		SignupRequestDto requestDto = makeTestSignUpRequestDto();

		//when
		memberRepository.save(member);

		//then
		assertThatThrownBy(() -> authService.signup(requestDto)).isInstanceOf(CustomException.class);
	}

	@Test
	void 로그인한_유저_찾기() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);

		//when
		Member findMember = authService.findMember();

		//then
		assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
	}

	@Test
	void 로그인() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);
		LoginRequestDto requestDto = LoginRequestDto.of(EMAIL, PASSWORD);

		//when
		TokenDto response = authService.login(requestDto);

		//then
		String key = PREFIX_REFRESH_TOKEN + response.getRefreshToken();
		String refreshToken = redisUtil.getData(key);

		assertThat(response.getAccessToken()).isNotNull();
		assertThat(response.getRefreshToken()).isNotNull();
		assertThat(refreshToken).isNotNull();

		//teardown
		redisUtil.deleteData(key);
	}
}