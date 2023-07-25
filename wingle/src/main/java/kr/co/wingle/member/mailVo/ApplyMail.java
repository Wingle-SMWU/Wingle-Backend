package kr.co.wingle.member.mailVo;

import kr.co.wingle.profile.entity.Nation;

public class ApplyMail extends Mail {
	public ApplyMail(String name, String nation) {
		if (nation.equals(Nation.RE.getEnNation())) {
			setMailFields("apply", "[WINGLE] 가입 신청이 완료되었습니다!");
		} else {
			setMailFields("applyEn", "[WINGLE] Sign up Application Completed");
		}
		super.values.put("name", name);
	}
}
