package kr.co.wingle.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

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

	public static Profile createProfile(String nickname, String introduction, boolean gender, boolean registration) {
		Profile profile = new Profile();
		profile.nickname = nickname;
		profile.introduction = introduction;
		profile.gender = gender;
		profile.registration = registration;

		return profile;
	}

}
