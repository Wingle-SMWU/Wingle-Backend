package kr.co.wingle.writing;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WritingUtil {
	private final AuthService authService;

	@Transactional(readOnly = true)
	public boolean isMine(Writing writing) {
		Member loggedInMember = authService.findMember();
		boolean isMine = writing.getMember().getId() == loggedInMember.getId() ? true : false;
		return isMine;
	}

}
