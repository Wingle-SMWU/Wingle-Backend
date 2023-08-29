package kr.co.wingle.message.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.common.util.StringUtil;
import kr.co.wingle.message.dto.MessageRequestDto;
import kr.co.wingle.message.dto.MessageResponseDto;
import kr.co.wingle.message.dto.MessageResponseWithRecipentDto;
import kr.co.wingle.message.dto.UnreadMessageCountResponseDto;
import kr.co.wingle.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/messages")
@Slf4j
public class MessageController {
	private final MessageService messageService;

	@PostMapping("")
	public ApiResponse<MessageResponseDto> send(@RequestBody @Valid MessageRequestDto messageRequestDto) {
		MessageResponseDto response = messageService.send(messageRequestDto);
		return ApiResponse.success(SuccessCode.MESSAGE_SEND_SUCCESS, response);
	}

	@GetMapping("/{roomId}")
	public ApiResponse<MessageResponseWithRecipentDto> getList(@PathVariable String roomId,
		@RequestParam String page, @RequestParam String size) {
		return ApiResponse.success(SuccessCode.GET_SUCCESS,
			messageService.getListByRoom(StringUtil.StringToLong(roomId),
				StringUtil.StringToInt(page),
				StringUtil.StringToInt(size)));
	}

	@GetMapping("/unread")
	public ApiResponse<UnreadMessageCountResponseDto> getUnreadMessageCount() {
		return ApiResponse.success(SuccessCode.GET_SUCCESS,
			messageService.getUnreadMessageCount());
	}
}
