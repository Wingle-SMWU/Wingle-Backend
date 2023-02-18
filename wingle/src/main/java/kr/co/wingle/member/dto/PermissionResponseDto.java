package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermissionResponseDto {
	@NotBlank
	private Long userId;
	@NotBlank
	private boolean permission;

	public static PermissionResponseDto of(Long userId, boolean permission) {
		PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
		permissionResponseDto.userId = userId;
		permissionResponseDto.permission = permission;
		return permissionResponseDto;
	}
}
