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

	public static ForumCode of(String forumName) {
		if (forumName.equals(FREE.name))
			return ForumCode.FREE;
		if (forumName.equals(EXCHANGE.name))
			return ForumCode.EXCHANGE;
		if (forumName.equals(NOTICE.name))
			return ForumCode.NOTICE;
		throw new IllegalArgumentException(ErrorCode.NO_FORUM.getMessage());
	}
}
