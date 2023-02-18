package kr.co.wingle.member.mailVo;

import java.util.Random;

public class CodeMail extends Mail {
	public static final long VALID_TIME = 1000 * 60 * 3L; // 3분

	public CodeMail() {
		super("code", "윙글(Wingle) 이메일 인증코드");
		super.setValue(createCode());
	}

	private String createCode() {
		final int CODE_LENGTH = 4;
		String code = "";
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++) {
			code += random.nextInt(10); // 0~9
		}
		return code;
	}
}
