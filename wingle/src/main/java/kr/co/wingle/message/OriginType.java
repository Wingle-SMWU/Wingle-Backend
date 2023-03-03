package kr.co.wingle.message;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OriginType {
	ARTICLE("Article"),
	COMMENT("Comment"),
	PROFILE("Profile");

	private final String type;

	public static OriginType from(String type) {
		for (OriginType tag : OriginType.values()) {
			if (tag.getType().equals(type)) {
				return tag;
			}
		}
		throw new NotFoundException(ErrorCode.BAD_PARAMETER);
	}
}
