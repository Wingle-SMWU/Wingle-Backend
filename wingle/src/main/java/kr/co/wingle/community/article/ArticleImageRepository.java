package kr.co.wingle.community.article;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {

	List<ArticleImage> getArticleImagesByArticleIdAndIsDeletedOrderByOrderNumber(Long articleId, boolean isDeleted);
}
