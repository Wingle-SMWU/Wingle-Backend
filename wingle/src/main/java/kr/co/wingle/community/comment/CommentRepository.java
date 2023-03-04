package kr.co.wingle.community.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByArticleIdAndIsDeleted(Long articleId, boolean isDeleted, Pageable pageable);
}
