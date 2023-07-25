package kr.co.wingle.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Nation {
	RE("RE", "Republic of Korea", "대한민국");

	private final String code;
	private final String enNation;
	private final String krNation;

}
