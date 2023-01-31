package kr.co.wingle.writing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.member.Member;
import kr.co.wingle.member.MemberRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WritingServiceTest {
	@Autowired
	private WritingService writingService;

	@Autowired
	private WritingRepository writingRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private DataSource dataSource;

	@BeforeAll
	public void beforeAll() throws Exception {
		System.out.println("BeforeAll");
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn,
				new ClassPathResource("/data/insert-member.sql"));
		}
	}

	@Test
	void 글쓰기_생성_정상() {
		// given
		Member member = memberRepository.findById(1L).get();
		String content = "😀 글 내용이야!";
		// when
		WritingDto writingDto = writingService.create(member.getId(), content);
		// then
		assertEquals(writingDto.getContent(), content);
		assertEquals(writingDto.getMember().getEmail(), member.getEmail());
	}

	@Test
	void 글쓰기_생성_존재하지_않는_유저() {
		// given
		Long memberId = -1L;
		String content = "😀 글 내용이야!";
		// when
		Throwable exception = assertThrows(RuntimeException.class, () -> {
			WritingDto writingDto = writingService.create(memberId, content);
		});
		// then
		assertEquals(exception.getMessage(), ErrorCode.NO_ID.getMessage());
	}
}
