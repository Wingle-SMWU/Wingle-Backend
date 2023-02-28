package kr.co.wingle.community.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.community.article.Article;
import kr.co.wingle.community.article.ArticleService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
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
}
