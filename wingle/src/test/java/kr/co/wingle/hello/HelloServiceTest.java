package kr.co.wingle.hello;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.hello.dto.HelloResponseDto;

@SpringBootTest
public class HelloServiceTest {
	@Autowired
	HelloService helloService;
	@Autowired
	HelloRepository helloRepository;

	@Test
	void 생성() {
		// given
		final String name = "😊헬로 create";

		// when
		HelloResponseDto savedHelloDto = helloService.create(name);

		// then
		Hello hello = helloRepository.findById(savedHelloDto.getId())
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));

		Assertions.assertEquals(hello.getName(), name);

		// teardown
		helloRepository.deleteById(savedHelloDto.getId());
	}

	@Test
	void 읽기_정상동작() {
		// given
		final String name = "헬로 read";
		Long id = helloService.create(name).getId();

		// when
		HelloResponseDto readHelloDto = helloService.read(id);

		// then
		Assertions.assertEquals(readHelloDto.getName(), name);

		// teardown
		helloRepository.deleteById(id);
	}

	@Test
	void 존재하지_않는_아이템_읽기() {
		// given
		final Long nonexistentId = -1L;

		// when
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
			() -> {
				HelloResponseDto readHelloDto = helloService.read(nonexistentId);
			});

		// then
		Assertions.assertEquals(thrown.getMessage(), ErrorCode.NO_ID.getMessage());
	}

	@Test
	void 논리_삭제된_아이템_읽기() {
		// given
		final String name = "논리_삭제된_아이템_읽기";
		Long id = helloService.create(name).getId();

		// when
		helloService.softDelete(id);

		// then
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
			() -> {
				HelloResponseDto readHelloDto = helloService.read(id);
				System.out.println(readHelloDto.getName());
			});

		Assertions.assertEquals(thrown.getMessage(), ErrorCode.ALREADY_DELETED.getMessage());
	}

	@Test
	void 수정() {
		// given
		final String name = "hello";
		final String updatedName = "hello update";
		Long id = helloService.create(name).getId();

		// when
		helloService.update(id, updatedName);

		// then
		HelloResponseDto readHelloDto = helloService.read(id);
		Assertions.assertEquals(readHelloDto.getName(), updatedName);

		// teardown
		helloRepository.deleteById(id);
	}

	@Test
	void 논리_삭제() {
		// given
		final String name = "논리_삭제";
		Long id = helloService.create(name).getId();

		// when
		helloService.softDelete(id);

		// then
		Hello softDeletedHello = helloRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));

		Assertions.assertEquals(softDeletedHello.isDeleted(), true);

		// teardown
		helloRepository.deleteById(id);
	}

}
