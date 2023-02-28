package kr.co.wingle.community.article;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.community.forum.Forum;
import kr.co.wingle.community.forum.ForumService;
import kr.co.wingle.community.writing.WritingService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.ProfileService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService extends WritingService {
	private final ArticleRepository articleRepository;
	private final ForumService forumService;
	private final AuthService authService;
	private final ArticleMapper articleMapper;
	private final ProfileService profileService;

	@Transactional
	public ArticleResponseDto create(ArticleRequestDto request) {
		Member member = authService.findMember();
		Forum forum = forumService.getForumById(request.getForumId());

		Article article = Article.builder()
			.forum(forum)
			.member(member)
			.content(request.getContent())
			.build();

		articleRepository.save(article);

		// TODO: Redis 최신목록에 등록

		// TODO: new ArrayList<String> 부분을 s3에서 받은 이미지 경로로 변경
		return articleMapper.toDto(article, new ArrayList<String>());
	}

	@Transactional(readOnly = true)
	public ArticleResponseDto getOne(Long forumId, Long articleId) {
		Article article = getArticleById(articleId);
		isValidForum(article, forumId);
		// TODO: new ArrayList<String> 부분을 s3에서 받은 이미지 경로로 변경
		return articleMapper.toDto(article, new ArrayList<String>());
	}

		// TODO: new ArrayList<String> 부분을 s3에서 받은 이미지 경로로 변경
		return articleMapper.toAnonymousDto(article, nickname, new ArrayList<String>(), isOwner(article, member));
	}

	@Transactional
	public Long delete(Long forumId, Long articleId) {
		Member member = authService.findMember();
		Article article = getArticleById(articleId);

		if (isValidMember(article, member) && isExist(article) && isValidForum(article, forumId)) {
			article.softDelete();
		}
		return article.getId();
	}

	public Article getArticleById(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ARTICLE_ID));
		isExist(article);
		return article;
	}

	private boolean isValidMember(Article article, Member member) {
		// 작성자 다르면 에러
		if (article.getMember().getId() != member.getId()) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_USER);
		}
		return true;
	}

	private boolean isExist(Article article) {
		// 이미 삭제된 게시물이면 에러
		if (article.isDeleted()) {
			throw new NotFoundException(ErrorCode.ALREADY_DELETED);
		}
		return true;
	}

	private boolean isValidForum(Article article, Long forumId) {
		// 게시판 안 맞으면 에러
		if (article.getForum().getId() != forumId) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}
		return true;
	}

}