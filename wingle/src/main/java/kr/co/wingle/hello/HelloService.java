package kr.co.wingle.hello;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloService {
	private final HelloRepository helloRepository;

	@Transactional
	public HelloDto create(HelloDto helloDto) {
		Hello hello = Hello.from(helloDto.getName());
		hello = helloRepository.save(hello);
		HelloDto savedHelloDto = HelloDto.of(hello.getId(), hello.getName());
		return savedHelloDto;
	}

	@Transactional(readOnly = true)
	public HelloDto read(Long id) {
		Hello hello = helloRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		HelloDto helloDto = HelloDto.of(hello.getId(), hello.getName());
		return helloDto;
	}

	@Transactional
	public HelloDto update(Long id, String name) {
		Hello hello = helloRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		hello.setName(name);
		Hello updatedHello = helloRepository.save(hello);
		HelloDto helloDto = HelloDto.of(updatedHello.getId(), updatedHello.getName());
		return helloDto;
	}

	@Transactional
	public HelloResponseDto softDelete(Long id) {
		Hello hello = helloRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		hello.softDelete();
		hello = helloRepository.save(hello);
		HelloResponseDto helloDto = HelloResponseDto.of(hello.getId(), hello.getName());
		return helloDto;
	}
}
