package kr.co.wingle.community.article;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.community.forum.Forum;
import kr.co.wingle.community.forum.ForumCode;
import kr.co.wingle.community.forum.ForumService;
import kr.co.wingle.community.writing.WritingService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService extends WritingService {
	private final ArticleRepository articleRepository;
	private final ForumService forumService;
	private final AuthService authService;
	private final ArticleMapper articleMapper;

	@Transactional
	public ArticleResponseDto create(ArticleRequestDto request) {
		Member member = authService.findMember();
		Forum forum = forumService.getForumById(request.getForumId());

		// 공지 작성 방지
		if (forum.getName().equals(ForumCode.NOTICE.getName())) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_USER);
		}

		Article article = Article.builder()
			.forum(forum)
			.member(member)
			.content(request.getContent())
			.build();

		articleRepository.save(article);

		// TODO: Redis 최신목록에 등록

		// TODO: new ArrayList<String> 부분을 s3에서 받은 이미지 경로로 변경
		return articleMapper.toResponseDto(article, new ArrayList<String>());
	}

	@Transactional(readOnly = true)
	public ArticleResponseDto getOne(Long forumId, Long articleId) {
		Article article = getArticleById(articleId);
		isValidForum(article, forumId);
		// TODO: new ArrayList<String> 부분을 s3에서 받은 이미지 경로로 변경
		return articleMapper.toResponseDto(article, new ArrayList<String>());
	}

	@Transactional(readOnly = true)
	public List<ArticleResponseDto> getList(Long forumId, int page, int size, boolean getMine) {
		Pageable pageable = PageRequest.of(page, size);
		List<Article> pages;
		if (getMine) {
			Member member = authService.findMember();
			pages = articleRepository.findByForumIdAndMemberIdAndIsDeleted(forumId, member.getId(), false, pageable);
		} else {
			pages = articleRepository.findByForumIdAndIsDeleted(forumId, false, pageable);
		}
		// TODO: new ArrayList<String> 부분을 s3에서 받은 이미지 경로로 변경
		List<ArticleResponseDto> result = pages.stream()
			.map(x -> articleMapper.toResponseDto(x, new ArrayList<String>()))
			.collect(
				Collectors.toList());
		return result;
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

	@Transactional(readOnly = true)
	public Article getArticleById(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ARTICLE_ID));
		isExist(article);
		return article;
	}

	@Transactional(readOnly = true)
	public boolean isValidForum(Article article, Long forumId) {
		// 게시판 안 맞으면 에러
		if (article.getForum().getId() != forumId) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}
		return true;
	}

}