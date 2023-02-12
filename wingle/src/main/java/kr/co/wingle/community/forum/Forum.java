package kr.co.wingle.community.forum;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Forum {
	@Column(nullable = false, unique = true)
	String name;
}
