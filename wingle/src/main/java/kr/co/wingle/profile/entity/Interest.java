package kr.co.wingle.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Interest extends BaseEntity {

	//관심사 명 (ex. kpop,운동...)
	@Column(nullable = false, unique = true)
	private String name;

	public static Interest createInterest(String name) {
		return new Interest(name);
	}

}
