package kr.co.wingle.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "term")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Term extends BaseEntity {

	@Column(nullable = false)
	private int code;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	@ColumnDefault("1")
	private boolean necessity = true;

	public static Term createTerm(int code, String name, boolean necessity) {
		return new Term(code, name, necessity);
	}
}
