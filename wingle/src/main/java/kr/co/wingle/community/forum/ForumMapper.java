package kr.co.wingle.community.forum;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ForumMapper {
	ForumResponseDto entityToDto(Forum forum);
}
