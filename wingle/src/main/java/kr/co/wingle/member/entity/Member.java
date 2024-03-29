package kr.co.wingle.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import kr.co.wingle.affliation.entity.School;
import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String idCardImageUrl;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@Setter
	private String password;

	@Column(nullable = false)
	@ColumnDefault("2")
	@Setter
	private int permission;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@ColumnDefault("'ROLE_USER'")
	private Authority authority = Authority.ROLE_USER;

	@Column
	@Setter
	private String rejectionReason;

	@Column
	@Setter
	private String memo;

	@ManyToOne(fetch = FetchType.LAZY)
	private School school;

	@Column(nullable = false)
	private String department;

	@Column(nullable = false)
	private String studentNumber;

	public static Member createMember(String name, String idCardImageUrl, String email, String password,
		Authority authority, School school, String department, String studentNumber) {
		Member member = new Member();
		member.name = name;
		member.idCardImageUrl = idCardImageUrl;
		member.email = email;
		member.password = password;
		member.permission = Permission.WAIT.getStatus();
		member.authority = authority;
		member.school = school;
		member.department = department;
		member.studentNumber = studentNumber;
		return member;
	}

	public static Member copyMember(Member from, Member to) {
		to.name = from.name;
		to.idCardImageUrl = from.idCardImageUrl;
		to.email = from.email;
		to.password = from.password;
		to.permission = from.permission;
		to.authority = from.authority;
		to.school = from.school;
		to.department = from.department;
		to.studentNumber = from.studentNumber;
		return to;
	}
}

