package kr.co.wingle.member.mailVo;

public class AcceptanceMail extends Mail {
	public AcceptanceMail(String name) {
		super("acceptance", "[WINGLE] 가입을 환영합니다!");
		super.values.put("name", name);
	}
}