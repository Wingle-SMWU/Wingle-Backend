package kr.co.wingle.community.article;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
	@Mapping(source = "article.id", target = "articleId")
	@Mapping(source = "article.createdTime", target = "createdTime")
	@Mapping(source = "article.updatedTime", target = "updatedTime")
	@Mapping(source = "nickname", target = "userNickname")
	@Mapping(target = "userId", expression = "java(article.getMember().getId())")
	@Mapping(target = "forumId", expression = "java(article.getForum().getId())")
	ArticleResponseDto toPublicDto(Article article, String nickname, List<String> images, boolean isMine);

	@Mapping(source = "article.id", target = "articleId")
	@Mapping(source = "article.createdTime", target = "createdTime")
	@Mapping(source = "article.updatedTime", target = "updatedTime")
	@Mapping(source = "nickname", target = "userNickname")
	@Mapping(target = "userId", expression = "java(0L)")
	@Mapping(target = "forumId", expression = "java(article.getForum().getId())")
	ArticleResponseDto toAnonymousDto(Article article, String nickname, List<String> images, boolean isMine);

}
