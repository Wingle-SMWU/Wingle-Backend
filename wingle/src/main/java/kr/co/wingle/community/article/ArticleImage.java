package kr.co.wingle.community.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import kr.co.wingle.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private Article article;

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	private int orderNumber;

	@Builder
	ArticleImage(Article article, String imageUrl, int orderNumber) {
		Assert.notNull(article, "article must not be null");
		Assert.notNull(imageUrl, "imageUrl must not be null");

		this.article = article;
		this.imageUrl = imageUrl;
		this.orderNumber = orderNumber;
	}
}
