package kr.co.wingle.community.util;

import org.springframework.stereotype.Component;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.community.article.Article;
import kr.co.wingle.community.comment.Comment;
import kr.co.wingle.community.forum.Forum;
import kr.co.wingle.community.forum.ForumService;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.AuthService;
import kr.co.wingle.profile.ProfileService;
import kr.co.wingle.profile.entity.Profile;
import kr.co.wingle.writing.Writing;
import kr.co.wingle.writing.WritingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommunityUtil {
	private final ProfileService profileService;
	private final ForumService forumService;
	private final AuthService authService;
	private final WritingUtil writingUtil;

	// 게시판, 작성자에 따라 개인정보 가공
	public ProcessedPersonalInformation processPersonalInformation(Writing writing) {
		Member loggedInMember = authService.findAcceptedLoggedInMember();
		Profile profile = profileService.getProfileByMemberId(writing.getMember().getId());

		Forum forum;
		if (writing instanceof Article) {
			forum = ((Article)writing).getForum();
		} else if (writing instanceof Comment) {
			forum = ((Comment)writing).getArticle().getForum();
		} else {
			throw new NotFoundException(ErrorCode.BAD_PARAMETER_TYPE);
		}
		// 게시판별 작성자명
		String nickname = forumService.getNicknameByForum(forum, profile);
		// 게시판별 멤버id
		Long processedMemberId = forumService.processMemberIdByForum(forum,
			writing.getMember().getId());
		// 게시판별 작성자 학교이름
		String schoolName = forumService.getSchoolNameByForum(forum, writing.getMember().getSchool());
		boolean isMine = writingUtil.isMine(writing);
		return ProcessedPersonalInformation.of(nickname, processedMemberId, schoolName, isMine);
	}
}
