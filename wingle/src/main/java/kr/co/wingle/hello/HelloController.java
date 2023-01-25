package kr.co.wingle.hello;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;

@RestController
public class HelloController {
	@GetMapping("/")
	String home() {
		return "Hello World!";
	}

	@GetMapping("/example")
	ApiResponse<HelloResponse> example(@Valid @RequestBody HelloRequest request) {
		//        if (true) {
		//            throw new CustomException(ErrorCode.EXAMPLE_ERROR);
		//        }
		HelloResponse response = HelloResponse.of(1L, request.getName());
		return ApiResponse.success(SuccessCode.EXAMPLE_SUCCESS, response);
	}
}