package kr.co.wingle.community.article;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	List<Article> findByForumIdAndIsDeleted(Long forumId, boolean isDeleted, Pageable pageable);

	List<Article> findByForumIdAndMemberIdAndIsDeleted(Long forumId, Long memberId, boolean isDeleted,
		Pageable pageable);
}
