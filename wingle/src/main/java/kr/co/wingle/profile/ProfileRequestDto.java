package kr.co.wingle.profile;

import java.util.List;

import kr.co.wingle.profile.entity.Interest;
import kr.co.wingle.profile.entity.Language;
import kr.co.wingle.profile.entity.MemberInterest;
import kr.co.wingle.profile.entity.Sns;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileRequestDto {
	private String nickname;

	private String introduction;

	private boolean gender;

	private boolean registration;

	private String nation;

	private String name;

	private int orderNumber;

	private Interest interest;

	private String url;

}
