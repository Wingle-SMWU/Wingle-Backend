package kr.co.wingle.community.forum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Setter
public class ForumResponseDto {
	private long id;
	private String name;

	public static ForumResponseDto of(long id, String name) {
		return new ForumResponseDto(id, name);
	}
}
