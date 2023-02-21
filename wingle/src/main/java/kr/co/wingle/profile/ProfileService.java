package kr.co.wingle.profile;

import org.springframework.stereotype.Service;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final ProfileRepository profileRepository;

	public Profile getProfileByMemberId(Long memberId) {
		return profileRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ID));
	}
}