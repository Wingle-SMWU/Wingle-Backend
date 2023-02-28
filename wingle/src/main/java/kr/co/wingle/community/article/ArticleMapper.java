package kr.co.wingle.community.article;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.wingle.community.forum.ForumService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.Profile;
import kr.co.wingle.profile.ProfileService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArticleMapper {
	private final ProfileService profileService;
	private final ForumService forumService;
	private final AuthService authService;

	public ArticleResponseDto toDto(Article article, List<String> images) {
		if (article == null) {
			return null;
		}
		Member loggedInMember = authService.findMember();
		Profile profile = profileService.getProfileByMemberId(article.getMember().getId());
		// 게시판별 작성자명
		String nickname = forumService.getNicknameByForum(article.getForum(), profile);
		// 게시판별 멤버id
		Long processedMemberId = forumService.processMemberIdByForum(article.getForum(), article.getMember().getId());
		boolean isMine = article.getMember().getId() == loggedInMember.getId() ? true : false;

		ArticleResponseDto.ArticleResponseDtoBuilder articleResponseDto = ArticleResponseDto.builder();

		if (article != null) {
			articleResponseDto.articleId(article.getId());
			articleResponseDto.createdTime(article.getCreatedTime());
			articleResponseDto.updatedTime(article.getUpdatedTime());
			articleResponseDto.content(article.getContent());
			articleResponseDto.likeCount(article.getLikeCount());
		}
		articleResponseDto.userNickname(nickname);
		List<String> list = images;
		if (list != null) {
			articleResponseDto.images(new ArrayList<String>(list));
		}
		articleResponseDto.isMine(isMine);
		articleResponseDto.userId(processedMemberId);
		articleResponseDto.forumId(article.getForum().getId());

		return articleResponseDto.build();
	}
}
