package kr.co.wingle.member.service;

import static kr.co.wingle.member.MemberTemplate.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.WaitingListResponseDto;
import kr.co.wingle.member.dto.WaitingUserResponseDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.entity.Permission;
import kr.co.wingle.profile.Profile;
import kr.co.wingle.profile.ProfileRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(value = EMAIL, password = PASSWORD)
@Transactional
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@BeforeAll
	void cleanUp() {
		memberRepository.deleteAll();
	}

	@Test
	void 수락_대기_목록_조회() throws Exception {
		//given
		for (int i = 0; i < 17; i++) {
			Member member = Member.createMember("name", "url", "wingle" + i + "@example.com",
				"password", Authority.ROLE_USER);
			memberRepository.save(member);
			if (i == 16) {
				member.setPermission(Permission.APPROVE.getStatus());
			}
		}

		//when
		List<WaitingListResponseDto> response1 = memberService.getWaitingList(0);
		List<WaitingListResponseDto> response2 = memberService.getWaitingList(1);

		//then
		assertThat(response1).hasSize(15);
		assertThat(response2).hasSize(1);
		assertThat(response1.get(0).getCreatedTime()).isAfter(response1.get(1).getCreatedTime());
	}

	@Test
	void 수락_대기_사용자_조회() throws Exception {
		//given
		Member member = makeTestMember();
		String nation = "KR";
		Profile profile = Profile.createProfile(member, "nickname", true, nation);
		memberRepository.save(member);
		profileRepository.save(profile);

		//when
		WaitingUserResponseDto response = memberService.getWaitingUserInfo(member.getId());

		//then
		assertThat(response.getName()).isEqualTo(member.getName());
		assertThat(response.getNation()).isEqualTo(nation);
	}
}