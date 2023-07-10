package kr.co.wingle.member.mailVo;

public class ApplyMail extends Mail {
	public ApplyMail(String name) {
		super("apply", "[WINGLE] 가입 신청이 완료되었습니다!");
		super.values.put("name", name);
	}
}
