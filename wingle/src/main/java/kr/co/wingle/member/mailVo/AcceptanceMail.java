package kr.co.wingle.member.mailVo;

import kr.co.wingle.profile.entity.Nation;

public class AcceptanceMail extends Mail {
	public AcceptanceMail(String name, String nation) {
		if (nation.equals(Nation.RE.getEnNation())) {
			setMailFields("acceptance", "[WINGLE] 가입을 환영합니다!");
		} else {
			setMailFields("acceptanceEn", "[WINGLE] Sign up Application Completed");
		}
		super.values.put("name", name);
	}

}