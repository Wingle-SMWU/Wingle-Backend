package kr.co.wingle.profile.util;

import org.springframework.stereotype.Component;

import kr.co.wingle.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileUtil {
	private final ProfileRepository profileRepository;

	public boolean isDuplicatedNickname(String nickname) {
		if (profileRepository.existsByNickname(nickname)) {
			return true;
		}
		return false;
	}
}
