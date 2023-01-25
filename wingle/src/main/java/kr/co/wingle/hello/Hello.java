package kr.co.wingle.hello;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hello")
@SQLDelete(sql = "UPDATE hello SET is_deleted = true WHERE id = ?")
public class Hello extends BaseEntity {

	@Column
	@Getter
	@Setter
	private String name;

	Hello(String name) {
		this.name = name;
	}

	public static Hello from(String name) {
		return new Hello(name);
	}
}
