package kr.co.wingle.member.mailVo;

public class AcceptanceMail extends Mail {
	public AcceptanceMail(String name) {
		super("acceptance", "윙글(Wingle) 가입을 환영합니다!", name);
	}
}
