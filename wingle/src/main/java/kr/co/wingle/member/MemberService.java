package kr.co.wingle.member;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements Duplication {
	private final MemberRepository memberRepository;

	@Override
	public boolean isDuplicated(String email) {
		if (memberRepository.existsByEmail(email)) {
			return true;
		}
		return false;
	}
}
