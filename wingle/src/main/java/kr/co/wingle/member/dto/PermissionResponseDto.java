package kr.co.wingle.member.dto;

import javax.validation.constraints.NotBlank;

import kr.co.wingle.common.util.AES256Util;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermissionResponseDto {
	@NotBlank
	private String userId;
	@NotBlank
	private boolean permission;

	public static PermissionResponseDto of(Long userId, boolean permission) {
		PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
		permissionResponseDto.userId = AES256Util.encrypt(userId.toString());
		permissionResponseDto.permission = permission;
		return permissionResponseDto;
	}
}
