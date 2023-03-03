package kr.co.wingle.community.comment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import kr.co.wingle.community.article.Article;
import kr.co.wingle.community.writing.Writing;
import kr.co.wingle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Writing {
	@ManyToOne(fetch = FetchType.LAZY)
	Article article;

	// TODO: 대댓글 구현
	// Comment comment;
	// List<Comment> nestedComments;

	private Comment(Article article, Member member, String content) {
		Assert.notNull(article, "article must not be null");
		Assert.notNull(member, "member must not be null");
		Assert.notNull(content, "content must not be null");
		this.article = article;
		this.member = member;
		this.content = content;
	}

	public static Comment of(Article article, Member member, String content) {
		return new Comment(article, member, content);
	}
}
