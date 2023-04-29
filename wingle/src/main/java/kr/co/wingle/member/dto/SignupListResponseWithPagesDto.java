package kr.co.wingle.member.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupListResponseWithPagesDto {
	private List<SignupListResponseDto> list;

	private Long totalPages;

	public static SignupListResponseWithPagesDto from(List<SignupListResponseDto> list, Long totalPages) {
		SignupListResponseWithPagesDto signupListResponseWithPagesDto = new SignupListResponseWithPagesDto();
		signupListResponseWithPagesDto.list = list;
		signupListResponseWithPagesDto.totalPages = totalPages;
		return signupListResponseWithPagesDto;
	}
}
