package kr.co.wingle.community.article;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
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

}
