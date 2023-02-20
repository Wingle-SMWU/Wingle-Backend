package kr.co.wingle.profile;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final ProfileRepository profileRepository;
}
