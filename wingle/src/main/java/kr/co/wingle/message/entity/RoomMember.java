package kr.co.wingle.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.Assert;

import kr.co.wingle.common.entity.BaseEntity;
import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMember extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false)
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Setter
	private int unreadMessageCount = 0;

	private RoomMember(Room room, Member member) {
		Assert.notNull(room, "room must not be null");
		Assert.notNull(member, "member must not be null");

		this.room = room;
		this.member = member;
	}

	public static RoomMember of(Room room, Member member) {
		return new RoomMember(room, member);
	}
}