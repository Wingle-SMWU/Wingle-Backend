package kr.co.wingle.hello;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.hello.dto.CreateRequestDto;
import kr.co.wingle.hello.dto.HelloResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HelloController {
	private final HelloService helloService;

	@GetMapping("/")
	String home() {
		return "Hello World!";
	}

	@GetMapping("/example")
	ApiResponse<HelloResponseDto> example(@Valid @RequestBody CreateRequestDto request) {
		//        if (true) {
		//            throw new CustomException(ErrorCode.EXAMPLE_ERROR);
		//        }
		HelloResponseDto response = HelloResponseDto.of(1L, request.getName());
		return ApiResponse.success(SuccessCode.EXAMPLE_SUCCESS, response);
	}

	@PostMapping("/hello/create")
	ApiResponse<HelloResponseDto> create(@Valid @RequestBody CreateRequestDto request) {
		HelloResponseDto savedHelloDto = helloService.create(request.getName());
		HelloResponseDto response = HelloResponseDto.of(savedHelloDto.getId(), savedHelloDto.getName());
		return ApiResponse.success(SuccessCode.EXAMPLE_SUCCESS, response);
	}

}
