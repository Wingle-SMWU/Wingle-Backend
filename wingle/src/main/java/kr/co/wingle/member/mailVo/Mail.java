package kr.co.wingle.member.mailVo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail {
	protected String fileName;
	protected String title;
	@Setter
	protected String value;

	protected Mail(String fileName, String title) {
		this.fileName = fileName;
		this.title = title;
	}
}
