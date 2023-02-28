package kr.co.wingle.community.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import kr.co.wingle.community.forum.Forum;
import kr.co.wingle.community.writing.Writing;
import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends Writing {
	@Column(nullable = false)
	private int likeCount = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	private Forum forum;

	@Column(nullable = false)
	private int comment_count = 0;

	@Builder
	Article(Forum forum, Member member, String content) {
		Assert.notNull(forum, "forum must not be null");
		Assert.notNull(member, "member must not be null");
		Assert.notNull(content, "content must not be null");

		this.forum = forum;
		this.setMember(member);
		this.setContent(content);
	}
}
