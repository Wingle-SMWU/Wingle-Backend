package kr.co.wingle.profile;

import org.springframework.stereotype.Component;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.DuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileUtil {
	private final ProfileRepository profileRepository;

	public boolean isDuplicatedNickname(String newNickname) {
		if (profileRepository.existsByNickname(newNickname)) {
			throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
		}

		return false;
	}
}
