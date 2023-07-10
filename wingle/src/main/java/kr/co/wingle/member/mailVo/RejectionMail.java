package kr.co.wingle.member.mailVo;

public class RejectionMail extends Mail {
	public RejectionMail(String reason) {
		super("rejection", "윙글(Wingle)에 가입하실 수 없습니다.");
		super.values.put("reason", reason);
	}
}
