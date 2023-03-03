package kr.co.wingle.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum OriginType {
	ARTICLE("Article"),
	COMMENT("Comment"),
	PROFILE("Profile");

	private final String type;
}
