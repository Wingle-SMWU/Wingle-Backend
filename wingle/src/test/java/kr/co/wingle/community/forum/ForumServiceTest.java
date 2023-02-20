package kr.co.wingle.community.forum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ForumServiceTest {
	@Autowired
	ForumService forumService;

	@Autowired
	ForumRepository forumRepository;

	@Test
	void 게시판_목록_조회() {
		List<String> names = Arrays.asList("공지", "자유");
		for (String name : names) {
			forumRepository.save(Forum.from(name));
		}

		List<ForumResponseDto> responseDtos = forumService.findAll();
		
		assertEquals(responseDtos.size(), names.size());
		assertEquals((responseDtos.stream().map(x -> x.getName()).collect(Collectors.toSet())),
			new HashSet<String>(names));

		forumRepository.deleteAll();
	}
}
