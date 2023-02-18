package kr.co.wingle.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CertificationResponseDto {
	private boolean isAvailable;

	public static CertificationResponseDto of(boolean isAvailable) {
		CertificationResponseDto certificationResponseDto = new CertificationResponseDto();
		certificationResponseDto.isAvailable = isAvailable;
		return certificationResponseDto;
	}
}
