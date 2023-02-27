package kr.co.wingle.profile;

import java.util.List;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.profile.entity.Interest;
import kr.co.wingle.profile.entity.Language;
import kr.co.wingle.profile.entity.MemberInterest;
import kr.co.wingle.profile.entity.Profile;
import kr.co.wingle.profile.entity.Sns;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileResponseDto {
	private Profile profile;

	private String idCardImageUrl;

	private String nickname;

	private Language languages;

	private MemberInterest interests;

	private String introduce;

	private Sns sns;

	private boolean registration;

	public static ProfileResponseDto of(String idCardImageUrl, String nickname, Language languages, MemberInterest interests, String introduce, Sns sns) {
		ProfileResponseDto profileResponseDto = new ProfileResponseDto();
		profileResponseDto.idCardImageUrl = idCardImageUrl;
		profileResponseDto.nickname = nickname;
		profileResponseDto.languages = languages;
		profileResponseDto.interests= interests;
		profileResponseDto.introduce= introduce;
		profileResponseDto.sns = sns;

		return profileResponseDto;
	}
}
