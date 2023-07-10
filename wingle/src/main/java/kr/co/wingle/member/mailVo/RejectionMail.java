package kr.co.wingle.member.mailVo;

public class RejectionMail extends Mail {
	public RejectionMail(String name, String reason) {
		super("rejection", "[WINGLE] 회원가입 결과 안내");
		super.values.put("name", name);
		super.values.put("reason", reason);
	}
}
