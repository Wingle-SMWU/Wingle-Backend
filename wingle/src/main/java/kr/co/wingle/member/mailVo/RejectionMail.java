package kr.co.wingle.member.mailVo;

import kr.co.wingle.profile.entity.Nation;

public class RejectionMail extends Mail {
	public RejectionMail(String name, String reason, String nation) {
		if (nation.equals(Nation.RE.getEnNation())) {
			setMailFields("rejection", "[WINGLE] 회원가입 결과 안내");
		} else {
			setMailFields("rejectionEn", "[WINGLE] Sign up Results");
		}
		super.values.put("name", name);
		super.values.put("reason", reason);
	}
}
