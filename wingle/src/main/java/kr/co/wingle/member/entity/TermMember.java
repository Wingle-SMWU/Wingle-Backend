package kr.co.wingle.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "term_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermMember extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "term_id", nullable = false)
	private Term term;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	@ColumnDefault("0")
	private boolean agreement = false;
}
