package kr.co.wingle.hello;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.wingle.common.constants.ErrorCode;

@SpringBootTest
public class HelloServiceTest {
	@Autowired
	HelloService helloService;

	@Test
	void 생성() {
		// given
		final String name = "hello create";
		HelloDto helloDto = HelloDto.from(name);

		// when
		HelloDto savedHelloDto = helloService.create(helloDto);

		// then
		HelloDto readHelloDto = helloService.read(savedHelloDto.getId());
		Assertions.assertEquals(readHelloDto.getName(), name);

		// teardown
		helloService.delete(savedHelloDto.getId());
	}

	@Test
	void 읽기() {
		// given
		final String name = "hello read";
		HelloDto helloDto = HelloDto.from(name);
		Long id = helloService.create(helloDto).getId();

		// when
		HelloDto readHelloDto = helloService.read(id);

		// then
		Assertions.assertEquals(readHelloDto.getName(), name);

		// teardown
		helloService.delete(id);
	}

	@Test
	void 존재하지_않는_아이템_읽기() {
		// given
		final Long nonexistentId = -1L;

		// when
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
			() -> {
				HelloDto readHelloDto = helloService.read(nonexistentId);
			});

		// then
		Assertions.assertEquals(thrown.getMessage(), ErrorCode.NO_ID.getMessage());
	}

	@Test
	void 수정() {
		// given
		final String name = "hello";
		final String updatedName = "hello update";
		HelloDto helloDto = HelloDto.from(name);
		Long id = helloService.create(helloDto).getId();

		// when
		helloService.update(id, updatedName);

		// then
		HelloDto readHelloDto = helloService.read(id);
		Assertions.assertEquals(readHelloDto.getName(), updatedName);

		// teardown
		helloService.delete(id);
	}

	@Test
	void 삭제() {
		// given
		final String name = "hello delete";
		HelloDto helloDto = HelloDto.from(name);
		Long id = helloService.create(helloDto).getId();

		// when
		helloService.delete(id);

		// then
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
			() -> {
				HelloDto readHelloDto = helloService.read(id);
				System.out.println(readHelloDto.getName());
			});

		Assertions.assertEquals(thrown.getMessage(), ErrorCode.NO_ID.getMessage());
	}
}
