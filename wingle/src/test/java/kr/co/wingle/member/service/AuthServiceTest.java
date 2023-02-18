package kr.co.wingle.member.service;

import static kr.co.wingle.member.MemberTemplate.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.DuplicateException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.RedisUtil;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.LoginRequestDto;
import kr.co.wingle.member.dto.LogoutRequestDto;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;
import kr.co.wingle.member.dto.TokenDto;
import kr.co.wingle.member.dto.TokenRequestDto;
import kr.co.wingle.member.entity.Member;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(value = EMAIL, password = PASSWORD)
class AuthServiceTest {
	@Autowired
	private AuthService authService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RedisUtil redisUtil;

	@BeforeAll
	@AfterEach
	void cleanUp() {
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
	void 회원가입_실패_이메일_중복() throws Exception {
		//given
		Member member = makeTestMember();
		SignupRequestDto requestDto = makeTestSignUpRequestDto();

		//when
		memberRepository.save(member);

		//then
		assertThatThrownBy(() -> authService.signup(requestDto)).isInstanceOf(DuplicateException.class)
			.hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());
	}

	@Test
	void 현재_로그인한_유저_찾기() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);

		//when
		Member findMember = authService.findMember();

		//then
		assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
	}

	@Test
	void 현재_로그인한_유저_찾기_실패() throws Exception {
		//given

		//when

		//then
		assertThatThrownBy(() -> authService.findMember()).isInstanceOf(NotFoundException.class)
			.hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
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
		String key = RedisUtil.PREFIX_REFRESH_TOKEN + response.getRefreshToken();
		String refreshToken = redisUtil.getData(key);

		assertThat(response.getAccessToken()).isNotNull();
		assertThat(response.getRefreshToken()).isNotNull();
		assertThat(refreshToken).isNotNull();

		//teardown
		redisUtil.deleteData(key);
	}

	@Test
	void 토큰_재발급() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);
		LoginRequestDto loginRequestDto = LoginRequestDto.of(EMAIL, PASSWORD);
		TokenDto tokenDto = authService.login(loginRequestDto);

		TokenRequestDto requestDto = TokenRequestDto.of(tokenDto.getRefreshToken());

		//when
		TokenDto response = authService.reissue(requestDto);
		String key = RedisUtil.PREFIX_REFRESH_TOKEN + response.getRefreshToken();
		String refreshToken = redisUtil.getData(key);

		//then
		assertThat(response.getAccessToken()).isNotNull();
		assertThat(response.getRefreshToken()).isNotNull();
		assertThat(refreshToken).isNotNull();

		//teardown
		redisUtil.deleteData(key);
		key = RedisUtil.PREFIX_REFRESH_TOKEN + tokenDto.getRefreshToken();
		redisUtil.deleteData(key);

	}

	@Test
	void 토큰_재발급_실패() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);
		LoginRequestDto loginRequestDto = LoginRequestDto.of(EMAIL, PASSWORD);
		TokenDto tokenDto = authService.login(loginRequestDto);

		//when
		TokenRequestDto requestDto = TokenRequestDto.of("refreshToken");

		//then
		assertThatThrownBy(() -> authService.reissue(requestDto)).isInstanceOf(NotFoundException.class)
			.hasMessage(ErrorCode.TOKEN_NOT_FOUND.getMessage());

		//teardown
		String key = RedisUtil.PREFIX_REFRESH_TOKEN + tokenDto.getRefreshToken();
		redisUtil.deleteData(key);
	}

	@Test
	void 로그아웃() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);
		LoginRequestDto loginRequestDto = LoginRequestDto.of(EMAIL, PASSWORD);
		TokenDto tokenDto = authService.login(loginRequestDto);
		LogoutRequestDto requestDto = LogoutRequestDto.of(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

		//when
		authService.logout(requestDto);

		//then
		String logoutAccessToken = redisUtil.getData(RedisUtil.PREFIX_LOGOUT + requestDto.getAccessToken());
		String logoutRefreshToken = redisUtil.getData(RedisUtil.PREFIX_REFRESH_TOKEN + requestDto.getRefreshToken());
		assertThat(logoutAccessToken).isEqualTo("logout");
		assertThat(logoutRefreshToken).isNull();

		//teardown
		redisUtil.deleteData(RedisUtil.PREFIX_LOGOUT + requestDto.getAccessToken());
	}

	@Test
	void 로그아웃_실패() throws Exception {
		//given
		Member member = makeTestMember();
		memberRepository.save(member);
		LoginRequestDto loginRequestDto = LoginRequestDto.of(EMAIL, PASSWORD);
		TokenDto tokenDto = authService.login(loginRequestDto);
		LogoutRequestDto requestDto = LogoutRequestDto.of(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

		//when
		authService.logout(requestDto);

		//then
		assertThatThrownBy(() -> authService.logout(requestDto)).isInstanceOf(NotFoundException.class)
			.hasMessage(ErrorCode.TOKEN_NOT_FOUND.getMessage());

		//teardown
		redisUtil.deleteData(RedisUtil.PREFIX_LOGOUT + requestDto.getAccessToken());
  }
  
  @Test
	void 이메일로_인증번호_전송_성공() {
	}

	@Test
	void 인증번호_일치_검사_성공() {

	}

	@Test
	void 인증번호_불일치_검사_실패() {

	}
}