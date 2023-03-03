package kr.co.wingle.community.forum;

import javax.persistence.Column;
import javax.persistence.Entity;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Forum extends BaseEntity {
	@Column(nullable = false, unique = true)
	@Getter
	String name;

	private Forum(String name) {
		this.name = name;
	}

	public static Forum from(String name) {
		return new Forum(name);
	}
}
