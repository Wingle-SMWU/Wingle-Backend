package kr.co.wingle.member.service;

import static kr.co.wingle.member.MemberTemplate.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.SignupListResponseDto;
import kr.co.wingle.member.dto.WaitingUserResponseDto;
import kr.co.wingle.member.entity.Authority;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.entity.Permission;
import kr.co.wingle.profile.Profile;
import kr.co.wingle.profile.ProfileRepository;

@SpringBootTest
@WithMockUser(value = EMAIL, password = PASSWORD)
@Transactional
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Test
	void 수락_대기_목록_조회() throws Exception {
		//given
		long count = getExistCount(Permission.WAIT);
		for (int i = 0; i < 17; i++) {
			Member member = Member.createMember("name", "url", "wingle" + i + "@example.com",
				"password", Authority.ROLE_USER);
			memberRepository.save(member);
			if (i == 16) {
				member.setPermission(Permission.APPROVE.getStatus());
			}
		}

		//when
		List<SignupListResponseDto> response1 = memberService.getWaitingList(0);
		List<SignupListResponseDto> response2 = memberService.getWaitingList(1);

		//then
		assertThat(response1).hasSize(15);
		assertThat(response2).hasSize((int)(1 + count));
		assertThat(response1.get(0).getCreatedTime()).isAfter(response1.get(1).getCreatedTime());
	}

	@Test
	void 수락_거절_목록_조회() throws Exception {
		//given
		long count = getExistCount(Permission.DENY);
		for (int i = 0; i < 17; i++) {
			Member member = Member.createMember("name", "url", "wingle" + i + "@example.com", "password",
				Authority.ROLE_USER);
			memberRepository.save(member);
			if (i == 16) {
				break;
			}
			member.setPermission(Permission.DENY.getStatus());
		}

		//when
		List<SignupListResponseDto> response1 = memberService.getRejectionList(0);
		List<SignupListResponseDto> response2 = memberService.getRejectionList(1);

		//then
		assertThat(response1).hasSize(15);
		assertThat(response2).hasSize((int)(1 + count));
		assertThat(response1.get(0).getCreatedTime()).isAfter(response1.get(1).getCreatedTime());
	}

	@Test
	void 수락_완료_목록_조회() throws Exception {
		//given
		long count = getExistCount(Permission.APPROVE);
		for (int i = 0; i < 17; i++) {
			Member member = Member.createMember("name", "url", "wingle" + i + "@example.com", "password",
				Authority.ROLE_USER);
			memberRepository.save(member);
			if (i == 16) {
				break;
			}
			member.setPermission(Permission.APPROVE.getStatus());
		}

		//when
		List<SignupListResponseDto> response1 = memberService.getAcceptanceList(0);
		List<SignupListResponseDto> response2 = memberService.getAcceptanceList(1);

		//then
		assertThat(response1).hasSize(15);
		assertThat(response2).hasSize((int)(1 + count));
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

	private long getExistCount(Permission wait) {
		long count = memberRepository.countByPermission(wait.getStatus());
		if (count > 14L) {
			count = 14L;
		}
		return count;
	}
}