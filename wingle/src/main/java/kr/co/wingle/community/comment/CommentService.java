package kr.co.wingle.community.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.community.article.Article;
import kr.co.wingle.community.article.ArticleService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.writing.WritingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService extends WritingService {
	private final ArticleService articleService;
	private final CommentMapper commentMapper;
	private final AuthService authService;
	private final CommentRepository commentRepository;

	@Transactional
	public CommentResponseDto create(CommentRequestDto request) {
		Member loggedInMember = authService.findMember();
		Article article = articleService.getArticleById(request.getArticleId());
		articleService.isValidForum(article, request.getForumId());

		Comment comment = Comment.of(article, loggedInMember, request.getContent());
		commentRepository.save(comment);
		return commentMapper.toResponseDto(comment);

	}

	@Transactional(readOnly = true)
	public List<CommentResponseDto> getList(Long forumId, Long articleId, int page, int size) {
		Article article = articleService.getArticleById(articleId);
		articleService.isValidForum(article, forumId);

		Pageable pageable = PageRequest.of(page, size);
		List<Comment> pages = commentRepository.findByArticleIdAndIsDeleted(articleId, false, pageable);
		List<CommentResponseDto> result = pages.stream()
			.map(commentMapper::toResponseDto).collect(Collectors.toList());
		return result;
	}

	@Transactional
	public Long delete(Long forumId, Long articleId, Long commentId) {
		Member member = authService.findMember();
		Comment comment = getCommentById(commentId);

		if (isValidMember(comment, member) && isExist(comment) && articleService.isValidForum(comment.getArticle(),
			forumId) && isValidArticle(comment, articleId)) {
			comment.softDelete();
		}
		return comment.getId();
	}

	@Transactional(readOnly = true)
	public Comment getCommentById(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ARTICLE_ID));
		isExist(comment);
		return comment;
	}

	@Transactional(readOnly = true)
	public boolean isValidArticle(Comment comment, Long articleId) {
		// 게시글 안 맞으면 에러
		if (!articleId.equals(comment.getArticle().getId())) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}
		return true;
	}

}
