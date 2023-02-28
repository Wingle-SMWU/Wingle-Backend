package kr.co.wingle.community.article;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.wingle.community.util.CommunityUtil;
import kr.co.wingle.community.util.ProcessedPersonalInformation;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArticleMapper {
	private final CommunityUtil communityUtil;

	public ArticleResponseDto toResponseDto(Article article, List<String> images) {
		if (article == null) {
			return null;
		}
		ProcessedPersonalInformation processedPersonalInformation = communityUtil.processPersonalInformation(article);

		ArticleResponseDto.ArticleResponseDtoBuilder articleResponseDto = ArticleResponseDto.builder();

		if (article != null) {
			articleResponseDto.articleId(article.getId());
			articleResponseDto.createdTime(article.getCreatedTime());
			articleResponseDto.updatedTime(article.getUpdatedTime());
			articleResponseDto.content(article.getContent());
			articleResponseDto.likeCount(article.getLikeCount());
		}
		articleResponseDto.userNickname(processedPersonalInformation.getNickname());
		List<String> list = images;
		if (list != null) {
			articleResponseDto.images(new ArrayList<String>(list));
		}
		articleResponseDto.isMine(processedPersonalInformation.isMine());
		articleResponseDto.userId(processedPersonalInformation.getProcessedMemberId());
		articleResponseDto.forumId(article.getForum().getId());

		return articleResponseDto.build();
	}
}
