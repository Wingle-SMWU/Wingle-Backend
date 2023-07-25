package kr.co.wingle.profile.util;

import org.springframework.stereotype.Component;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.member.service.MemberService;
import kr.co.wingle.profile.ProfileRepository;
import kr.co.wingle.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileUtil {
	private final ProfileRepository profileRepository;
	private final MemberService memberService;

	public boolean isDuplicatedNickname(String nickname) {
		if (profileRepository.existsByNickname(nickname)) {
			return true;
		}
		return false;
	}

	// Member가 기존 사용하던 닉네임은 계속 이용 가능 (중복 아님)
	public boolean isDuplicatedNicknameByMemberId(String nickname, Long MemberId) {
		Profile profile = profileRepository.findByMemberId(MemberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PROFILE_NOT_FOUND));
		if (!profile.getNickname().equals(nickname) &&
			isDuplicatedNickname(nickname)) {
			return true;
		}
		return false;
	}

	// TODO: Service랑 비교하면서 리팩터링
	public Profile getProfile(Long memberId) {
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.NO_PROFILE));
		return profile;
	}
}
