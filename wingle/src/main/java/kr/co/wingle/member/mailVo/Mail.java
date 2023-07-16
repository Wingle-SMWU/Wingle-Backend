package kr.co.wingle.member.mailVo;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail {
	protected String fileName;
	protected String title;
	protected Map<String, String> values = new HashMap<>();

	protected Mail(String fileName, String title) {
		this.fileName = fileName;
		this.title = title;
	}
}
