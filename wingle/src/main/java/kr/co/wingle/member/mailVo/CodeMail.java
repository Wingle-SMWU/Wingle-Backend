package kr.co.wingle.member.mailVo;

import java.util.Random;

public class CodeMail extends Mail {
	public static final long VALID_TIME = 1000 * 60 * 5L; // 5분

	public CodeMail() {
		super("code", "[WINGLE] 회원가입 인증번호");
		super.values.put("code", createCode());

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
