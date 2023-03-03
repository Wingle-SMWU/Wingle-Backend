package kr.co.wingle.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.Assert;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.writing.Writing;
import lombok.Getter;

@Entity
@Table(name = "message")
@Getter
public class Message extends Writing {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false)
	private Room room;

	private Message(Member member, String content, Room room) {
		Assert.notNull(member, "member must not be null");
		Assert.notNull(content, "content must not be null");
		Assert.notNull(room, "room must not be null");
		this.member = member;
		this.content = content;
		this.room = room;
	}

	public static Message of(Member member, String content, Room room) {
		return new Message(member, content, room);
	}
}