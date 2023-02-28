package kr.co.wingle.profile;

import org.springframework.stereotype.Service;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.profile.entity.Profile;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final ProfileRepository profileRepository;

	public Profile getProfileByMemberId(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_PROFILE));
	}
}