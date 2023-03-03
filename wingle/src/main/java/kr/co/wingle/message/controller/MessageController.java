package kr.co.wingle.message.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.message.dto.MessageRequestDto;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/messages")
@Slf4j
public class MessageController {
	private final MessageService messageService;

	@PostMapping("/")
	public ApiResponse<MessageResponseDto> send(@RequestBody @Valid MessageRequestDto messageRequestDto) {
		MessageResponseDto response = messageService.send(messageRequestDto);
		return ApiResponse.success(SuccessCode.MESSAGE_SEND_SUCCESS, response);
	}
}
