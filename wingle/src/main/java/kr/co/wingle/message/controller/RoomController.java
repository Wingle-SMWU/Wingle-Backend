package kr.co.wingle.message.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.wingle.common.constants.SuccessCode;
import kr.co.wingle.common.dto.ApiResponse;
import kr.co.wingle.common.util.StringUtil;
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

	@GetMapping("/rooms")
	public ApiResponse<List<RoomResponseDto>> getList(@RequestParam String page, @RequestParam String size) {
		return ApiResponse.success(SuccessCode.GET_SUCCESS,
			roomService.getMyList(StringUtil.StringToInt(page),
				StringUtil.StringToInt(size)));
	}
}
