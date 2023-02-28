package kr.co.wingle.community.forum;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.profile.Profile;
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

	public Forum getForumById(Long forumId) {
		return forumRepository.findById(forumId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.NO_ID));
	}

	static public String getNicknameByForum(Forum forum, Profile profile) {
		final String nationCodeKor = "kor";
		final String anonymousKor = "한국 윙그리";
		final String anonymousNoneKor = "외국 윙그리";
		final String admin = "관리자";

		ForumCode forumCode = ForumCode.from(forum.getName());

		String nickname = switch (forumCode) {
			case FREE -> profile.getNation().equals(nationCodeKor) ? anonymousKor : anonymousNoneKor;
			case EXCHANGE -> profile.getNickname();
			case NOTICE -> admin;
			default -> throw new IllegalStateException(ErrorCode.NO_FORUM.getMessage());
		};
		return nickname;
	}
}