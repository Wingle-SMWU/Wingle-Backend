package kr.co.wingle.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.member.dto.SignupRequestDto;
import kr.co.wingle.member.dto.SignupResponseDto;

@SpringBootTest
class AuthServiceTest {
	@Autowired
	private AuthService authService;
	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void deleteMember() {
		memberRepository.deleteAll();
	}

	@Test
	void 회원가입() throws Exception {
		//given
		SignupRequestDto requestDto = MemberTemplate.makeTestSignUpRequestDto();

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
		Member member = MemberTemplate.makeTestMember();
		SignupRequestDto requestDto = MemberTemplate.makeTestSignUpRequestDto();

		//when
		memberRepository.save(member);

		//then
		assertThatThrownBy(() -> authService.signup(requestDto)).isInstanceOf(CustomException.class);
	}
}