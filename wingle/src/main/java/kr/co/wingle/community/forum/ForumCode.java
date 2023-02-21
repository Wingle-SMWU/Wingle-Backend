package kr.co.wingle.community.forum;

import kr.co.wingle.common.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ForumCode {
	FREE(1L, "자유"),
	EXCHANGE(2L, "교류"),
	NOTICE(3L, "공지");

	private final Long id;
	private final String name;

	public static ForumCode from(String forumName) {
		for (ForumCode code : ForumCode.values()) {
			if (code.getName().equals(forumName))
				return code;
		}
		throw new IllegalArgumentException(ErrorCode.NO_FORUM.getMessage());
	}
}
