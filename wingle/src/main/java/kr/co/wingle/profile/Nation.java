package kr.co.wingle.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Nation extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String code;

	public static Nation createNation(String code) {
		return new Nation(code);
	}

}
