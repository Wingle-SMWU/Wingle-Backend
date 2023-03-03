package kr.co.wingle.message.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.message.dto.RoomRequestDto;
import kr.co.wingle.message.dto.RoomResponseDto;
import kr.co.wingle.message.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/messages")
@Slf4j
public class RoomController {
	private final RoomService roomService;

	@PostMapping("/room")
	public ApiResponse<RoomResponseDto> getRoom(@RequestBody @Valid RoomRequestDto roomRequestDto) {
		RoomResponseDto responseDto = roomService.getRoom(roomRequestDto);
		return ApiResponse.success(SuccessCode.ROOM_FIND_SUCCESS, responseDto);
	}
}
