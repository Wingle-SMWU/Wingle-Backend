package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcceptanceResponseDto {
	@NotBlank
	private Long userId;
	@NotBlank
	private boolean permission;

	public static AcceptanceResponseDto of(Long userId) {
		AcceptanceResponseDto acceptanceResponseDto = new AcceptanceResponseDto();
		acceptanceResponseDto.userId = userId;
		acceptanceResponseDto.permission = true;
		return acceptanceResponseDto;
	}
}
