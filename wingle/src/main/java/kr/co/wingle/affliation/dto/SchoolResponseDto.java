package kr.co.wingle.affliation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolResponseDto {
	private Long schoolId;
	private String schoolCode;
	private String schoolName;

	public static SchoolResponseDto from(Long schoolId, String schoolCode, String schoolName) {
		SchoolResponseDto schoolResponseDto = new SchoolResponseDto();
		schoolResponseDto.schoolId = schoolId;
		schoolResponseDto.schoolCode = schoolCode;
		schoolResponseDto.schoolName = schoolName;
		return schoolResponseDto;
	}
}
