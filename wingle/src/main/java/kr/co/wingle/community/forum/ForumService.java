package kr.co.wingle.community.forum;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForumService {
	private final ForumRepository forumRepository;
	private final ForumMapper forumMapper;

	@Transactional(readOnly = true)
	public List<ForumResponseDto> findAll() {
		List<Forum> forums = forumRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		return forums.stream().map(x -> forumMapper.entityToDto(x)).collect(Collectors.toList());
	}
}
