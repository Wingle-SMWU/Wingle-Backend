package kr.co.wingle.message.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import kr.co.wingle.common.entity.BaseEntity;
import kr.co.wingle.message.OriginType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {
	private Long originId;
	private OriginType originType;

	private Room(Long originId, OriginType originType) {
		Assert.notNull(originId, "originId must not be null");
		Assert.notNull(originType, "originType must not be null");

		this.originId = originId;
		this.originType = originType;
	}

	public static Room of(Long originId, OriginType originType) {
		return new Room(originId, originType);
	}
}