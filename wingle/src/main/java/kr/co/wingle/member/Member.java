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
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

	public static Member createMember(String name, String idCardImageUrl, String email, String password) {
		return new Member(name, idCardImageUrl, email, password, Permission.WAIT.getStatus());
	}
}

