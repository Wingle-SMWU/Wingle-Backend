package kr.co.wingle.community.forum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ForumResponseDto {
	public long id;
	public long name;
}
