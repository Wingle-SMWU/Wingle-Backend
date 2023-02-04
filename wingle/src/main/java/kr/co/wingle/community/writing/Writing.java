package kr.co.wingle.community.writing;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import kr.co.wingle.common.entity.BaseEntity;
import kr.co.wingle.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // 하위 테이블의 구분 컬럼 생성(default = DTYPE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Writing extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	private Member member;

	@Column(nullable = false)
	@Getter
	private String content;

	public static Writing createWriting(Member member, String content) {
		return new Writing(member, content);
	}
}
