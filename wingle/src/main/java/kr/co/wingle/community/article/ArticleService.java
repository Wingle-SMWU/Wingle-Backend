package kr.co.wingle.community.article;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.community.forum.Forum;
import kr.co.wingle.community.forum.ForumRepository;
import kr.co.wingle.community.writing.WritingService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.Profile;
import kr.co.wingle.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService extends WritingService {
	private final ArticleRepository articleRepository;
	private final ForumRepository forumRepository;
	private final AuthService authService;
	private final ArticleMapper articleMapper;
	private final ProfileRepository profileRepository;

	@Transactional
	public ArticleResponseDto create(ArticleRequestDto request) {
		Member member = authService.findMember();
		Profile profile = profileRepository.findById(member.getId())
			.orElseThrow(() -> new IllegalArgumentException(
				ErrorCode.NO_ID.getMessage()));
		Forum forum = forumRepository.findById(request.getForumId())
			.orElseThrow(() -> new IllegalArgumentException(
				ErrorCode.NO_ID.getMessage()));

		Article article = Article.builder()
			.forum(forum)
			.member(member)
			.content(request.getContent())
			.build();

		return articleMapper.entityToDto(articleRepository.save(article), profile, new ArrayList<String>(), true);
	}

	@Transactional
	public Long delete(Long forumId, Long articleId) {

		Member member = authService.findMember();
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ARTICLE_ID));

		if (isValidMember(article, member) && isExist(article) && isValidForum(article, forumId)) {
			article.softDelete();
		}
		return article.getId();
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