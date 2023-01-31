package kr.co.wingle.member;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kr.co.wingle.common.constants.ErrorCode;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Test
	void 멤버_DB_저장() {
		String name = "name";
		String idCardImageUrl = "url";
		String email = "email";
		String password = "password";
		Member member = Member.createMember(name, idCardImageUrl, email, password);

		Member savedMember = memberRepository.save(member);

		assertEquals(member.getName(), savedMember.getName());
		assertEquals(member.getIdCardImageUrl(), savedMember.getIdCardImageUrl());
		assertEquals(member.getEmail(), savedMember.getEmail());
		assertEquals(member.getPassword(), savedMember.getPassword());
		assertEquals(2, savedMember.getPermission());
	}

	@Test
	void 멤버_수락여부_변경() {
		Member member = Member.createMember("name", "url", "email2", "password");

		Member savedMember = memberRepository.save(member);
		savedMember.setPermission(Permission.APPROVE.getStatus());

		Member findMember = memberRepository.findById(savedMember.getId())
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));

		assertEquals(1, findMember.getPermission());
	}
}