package kr.co.wingle.profile;

import static kr.co.wingle.member.MemberTemplate.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.profile.dto.InterestsRequestDto;
import kr.co.wingle.profile.dto.InterestsResponseDto;
import kr.co.wingle.profile.dto.IntroductionRequestDto;
import kr.co.wingle.profile.dto.IntroductionResponseDto;
import kr.co.wingle.profile.dto.LanguagesRequestDto;
import kr.co.wingle.profile.dto.LanguagesResponseDto;
import kr.co.wingle.profile.dto.ProfileRequestDto;
import kr.co.wingle.profile.dto.ProfileResponseDto;
import kr.co.wingle.profile.entity.Profile;

@SpringBootTest
@WithMockUser(value = EMAIL, password = PASSWORD)
@Transactional
class ProfileServiceTest {
	@Autowired
	private ProfileService profileService;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private S3Util s3Util;

	@Test
	void 프로필_저장() throws Exception {
		//given
		saveMemberAndProfile();
		ProfileRequestDto request1 = new ProfileRequestDto(getProfileImage(), false, "wingle");
		ProfileRequestDto request2 = new ProfileRequestDto(null, false, "wingle");
		ProfileRequestDto request3 = new ProfileRequestDto(null, true, "mingle");

		//when
		ProfileResponseDto response1 = profileService.saveProfile(request1);
		ProfileResponseDto response2 = profileService.saveProfile(request2);
		ProfileResponseDto response3 = profileService.saveProfile(request3);

		//then
		assertThat(request1.getNickname()).isEqualTo(response1.getNickname());
		assertThat(response1.getImage()).isNotNull();

		assertThat(response2.getImage()).isEqualTo(response1.getImage());
		assertThat(response2.getNickname()).isEqualTo(response1.getNickname());

		assertThat(response3.getImage()).isNull();
		assertThat(response3.getNickname()).isEqualTo(request3.getNickname());

		//teardown
		s3Util.delete(response1.getImage());
	}

	@Test
	void 사용_가능_언어_저장() throws Exception {
		//given
		saveMemberAndProfile();

		List<String> languages1 = new ArrayList<>();
		languages1.add("KR");
		languages1.add("EN");
		LanguagesRequestDto request1 = new LanguagesRequestDto(languages1);

		List<String> languages2 = new ArrayList<>();
		languages2.add("FR");
		LanguagesRequestDto request2 = new LanguagesRequestDto(languages2);

		//when
		LanguagesResponseDto response1 = profileService.saveLanguages(request1);
		LanguagesResponseDto response2 = profileService.saveLanguages(request2);

		//then
		assertThat(response1.getLanguages()).hasSize(languages1.size());
		assertThat(response1.getLanguages().get(0).getOrder()).isEqualTo(1);
		assertThat(response1.getLanguages().get(0).getInterest()).isEqualTo(languages1.get(0));
		assertThat(response1.getLanguages().get(1).getOrder()).isEqualTo(2);
		assertThat(response1.getLanguages().get(1).getInterest()).isEqualTo(languages1.get(1));

		assertThat(response2.getLanguages()).hasSize(languages2.size());
		assertThat(response2.getLanguages().get(0).getOrder()).isEqualTo(1);
		assertThat(response2.getLanguages().get(0).getInterest()).isEqualTo(languages2.get(0));
	}

	@Test
	void 자기소개_저장() throws Exception {
		//given
		saveMemberAndProfile();

		IntroductionRequestDto request1 = new IntroductionRequestDto("안녕하세요.");
		IntroductionRequestDto request2 = new IntroductionRequestDto("윙글입니다.");

		//when
		IntroductionResponseDto response1 = profileService.saveIntroduction(request1);
		IntroductionResponseDto response2 = profileService.saveIntroduction(request2);

		//then
		assertThat(response1.getIntroduction()).isEqualTo(request1.getIntroduction());
		assertThat(response2.getIntroduction()).isEqualTo(request2.getIntroduction());
	}

	@Test
	void 관심사_저장() throws Exception {
		//given
		saveMemberAndProfile();

		List<String> interests1 = new ArrayList<>();
		interests1.add("KPOP");
		interests1.add("운동");
		InterestsRequestDto request1 = new InterestsRequestDto(interests1);

		List<String> interests2 = new ArrayList<>();
		interests2.add("운동");
		interests2.add("여행");
		interests2.add("드라마");
		InterestsRequestDto request2 = new InterestsRequestDto(interests2);

		//when
		InterestsResponseDto response1 = profileService.saveInterests(request1);
		InterestsResponseDto response2 = profileService.saveInterests(request2);

		//then
		assertThat(response1.getInterests()).hasSize(2);
		assertThat(response1.getInterests().get(0)).isEqualTo("KPOP");
		assertThat(response1.getInterests().get(1)).isEqualTo("운동");

		assertThat(response2.getInterests()).hasSize(3);
		assertThat(response2.getInterests().get(0)).isEqualTo("운동");
		assertThat(response2.getInterests().get(1)).isEqualTo("여행");
		assertThat(response2.getInterests().get(2)).isEqualTo("드라마");
	}

	private void saveMemberAndProfile() {
		Member member = makeTestMember();
		memberRepository.save(member);
		profileRepository.save(Profile.createProfile(member, NICKNAME, GENDER, NATION));
	}
}