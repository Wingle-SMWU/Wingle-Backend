package kr.co.wingle.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import kr.co.wingle.common.entity.BaseEntity;
import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	private String nickname;

	@Column(columnDefinition = "TEXT")
	private String introduction;

	@Column(nullable = false)
	private boolean gender;

	@Column(nullable = false)
	@ColumnDefault("0")
	@Setter
	private boolean registration;

	@Column(nullable = false)
	private String nation;

	public static Profile createProfile(Member member, String nickname, String introduction, boolean gender,
		boolean registration, String nation) {
		Profile profile = new Profile();
		profile.member = member;
		profile.nickname = nickname;
		profile.introduction = introduction;
		profile.gender = gender;
		profile.registration = registration;
		profile.nation = nation;
		return profile;
	}

}