package kr.co.wingle.community.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessedPersonalInformation {
	private String nickname;
	private Long processedMemberId;
	private String schoolName;
	private boolean isMine;

	public static ProcessedPersonalInformation of(String nickname, Long processedMemberId, String schoolName,
		boolean isMine) {
		return new ProcessedPersonalInformation(nickname, processedMemberId, schoolName, isMine);
	}
}
