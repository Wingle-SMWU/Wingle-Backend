package kr.co.wingle.community.article;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.S3Util;
import kr.co.wingle.community.forum.Forum;
import kr.co.wingle.community.forum.ForumCode;
import kr.co.wingle.community.forum.ForumService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.writing.WritingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService extends WritingService {
	private final ArticleRepository articleRepository;
	private final ArticleImageRepository articleImageRepository;
	private final ForumService forumService;
	private final AuthService authService;
	private final ArticleMapper articleMapper;
	private final S3Util s3Util;

	@Transactional
	public ArticleResponseDto create(ArticleRequestDto request) {
		Member member = authService.findAcceptedLoggedInMember();
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

		ArrayList<String> imageUrlList = new ArrayList<>();
		for (int i = 0; i < request.getImages().size(); i++) {
			String imageUrl = s3Util.articleImageUpload(request.getImages().get(i));
			imageUrlList.add(imageUrl);
			articleImageRepository.save(new ArticleImage(article, imageUrl, i + 1));
		}

		// TODO: Redis 최신목록에 등록

		return articleMapper.toResponseDto(article, imageUrlList);
	}

	public ArticleResponseDto editArticle(Long forumId, Long articleId, ArticleEditRequestDto request) {
		Article article = getArticleById(articleId);
		isValidForum(article, forumId);

		article.setContent(request.getContent());

		List<ArticleImage> allByArticle = articleImageRepository.findAllByArticleId(articleId);
		articleImageRepository.deleteAll(allByArticle);

		ArrayList<String> imageUrlList = (ArrayList<String>)request.getOriginImages();
		imageUrlList.addAll(request.getNewImages().stream().map(image -> s3Util.articleImageUpload(image))
			.collect(Collectors.toList()));

		for (int i = 0; i < imageUrlList.size(); i++) {
			articleImageRepository.save(new ArticleImage(article, imageUrlList.get(i), i + 1));
		}

		return articleMapper.toResponseDto(article, imageUrlList);
	}

	@Transactional(readOnly = true)
	public ArticleResponseDto getOne(Long forumId, Long articleId) {
		Article article = getArticleById(articleId);
		isValidForum(article, forumId);

		List<String> imageUrlList = articleImageRepository.getArticleImagesByArticleIdAndIsDeletedOrderByOrderNumber(
				article.getId(), false)
			.stream().map(articleImage -> articleImage.getImageUrl()).collect(Collectors.toList());

		return articleMapper.toResponseDto(article, imageUrlList);
	}

	@Transactional(readOnly = true)
	public List<ArticleResponseDto> getList(Long forumId, int page, int size, boolean getMine) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());
		List<Article> pages;
		if (getMine) {
			Member member = authService.findAcceptedLoggedInMember();
			pages = articleRepository.findByForumIdAndMemberIdAndIsDeleted(forumId, member.getId(), false, pageable);
		} else {
			pages = articleRepository.findByForumIdAndIsDeleted(forumId, false, pageable);
		}

		return pages.stream()
			.map(x -> articleMapper.toResponseDto(x,
				articleImageRepository.getArticleImagesByArticleIdAndIsDeletedOrderByOrderNumber(x.getId(), false)
					.stream().map(articleImage -> articleImage.getImageUrl()).collect(Collectors.toList())))
			.collect(
				Collectors.toList());
	}

	@Transactional
	public Long delete(Long forumId, Long articleId) {
		Member member = authService.findAcceptedLoggedInMember();
		Article article = getArticleById(articleId);

		if (isValidMember(article, member) && isExist(article) && isValidForum(article, forumId)) {
			articleImageRepository.getArticleImagesByArticleIdAndIsDeletedOrderByOrderNumber(articleId, false)
				.forEach(articleImage -> articleImage.softDelete());
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
		if (!forumId.equals(article.getForum().getId())) {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER);
		}
		return true;
	}

}