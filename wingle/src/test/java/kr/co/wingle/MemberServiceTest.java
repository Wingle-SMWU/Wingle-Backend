package kr.co.wingle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.wingle.member.Authority;
import kr.co.wingle.member.Member;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.MemberService;

@SpringBootTest
public class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;

	@Test
	void 이미_등록된_이메일_중복_검사() {
		// given
		final String email = "wingle@gmail.com";
		Member inputMember = null;
		boolean isMemberExisted = false;

		// when
		isMemberExisted = memberRepository.existsByEmail("wingle@gmail.com");
		if (!isMemberExisted) {
			inputMember = Member.createMember("", "", email, "", Authority.ROLE_USER);
			memberRepository.save(inputMember);
		}

		// then
		Assertions.assertEquals(true, memberService.isDuplicated(email));

		// teardown
		if (!isMemberExisted)
			memberRepository.deleteById(inputMember.getId());
	}
}
