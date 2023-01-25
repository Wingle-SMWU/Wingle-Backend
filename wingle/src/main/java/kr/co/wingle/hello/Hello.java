package kr.co.wingle.hello;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hello")
@SQLDelete(sql = "UPDATE hello SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
public class Hello extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@Getter
	private Long id;

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

	@Column(name = "is_deleted", columnDefinition = "TINYINT", length = 1, nullable = false)
	@ColumnDefault("0")
	private Boolean isDeleted = false;
}
