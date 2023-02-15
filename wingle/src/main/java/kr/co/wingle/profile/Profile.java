package kr.co.wingle.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {
	@Column(nullable = false, unique = true)
	private String nickname;
}
