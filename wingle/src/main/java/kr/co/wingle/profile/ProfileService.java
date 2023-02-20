package kr.co.wingle.profile;

import org.springframework.stereotype.Service;

import kr.co.wingle.common.constants.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
<<<<<<< HEAD:wingle/src/main/java/kr/co/wingle/profile/ProfileService.java
public class ProfileService {
	private final ProfileRepository profileRepository;
=======
public class MemberService {
	private final MemberRepository memberRepository;

	public boolean validate(long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		if (member.isDeleted() == true) {
			throw new IllegalArgumentException(ErrorCode.NO_ID.getMessage());
		}
		// TODO: 관리자페이지에서 승인 받은 회원인지 검사
		return true;
	}
>>>>>>> 게시판-조회-#20:wingle/src/main/java/kr/co/wingle/member/MemberService.java
}
