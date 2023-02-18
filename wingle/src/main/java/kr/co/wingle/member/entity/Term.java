package kr.co.wingle.member.entity;

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
@Table(name = "term")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term extends BaseEntity {

	@Column(nullable = false)
	@Setter
	private Long code;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	@ColumnDefault("1")
	private boolean necessity = true;

	public static Term createTerm(long code, String name, boolean necessity) {
		Term term = new Term();
		term.code = code;
		term.name = name;
		term.necessity = necessity;
		return term;
	}
}
