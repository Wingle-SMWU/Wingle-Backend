package kr.co.wingle.member.dto;

import java.time.LocalDateTime;

import kr.co.wingle.common.util.AES256Util;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.profile.entity.Profile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingUserResponseDto {
	private String userId;
	private String name;
	private LocalDateTime createdTime;
	private String idCardImage;
	private boolean gender;
	private String email;
	private String nickname;
	private String reason;
	private String memo;
	private String nation;
	private String schoolName;
	private String departmentName;
	private String studentNumber;

	public static WaitingUserResponseDto from(Member member, Profile profile) {
		WaitingUserResponseDto waitingUserResponseDto = new WaitingUserResponseDto();
		waitingUserResponseDto.userId = AES256Util.encrypt(member.getId().toString());
		waitingUserResponseDto.name = member.getName();
		waitingUserResponseDto.createdTime = member.getCreatedTime();
		waitingUserResponseDto.idCardImage = member.getIdCardImageUrl();
		waitingUserResponseDto.reason = member.getRejectionReason();
		waitingUserResponseDto.memo = member.getMemo();
		waitingUserResponseDto.email = member.getEmail();
		waitingUserResponseDto.nation = profile.getNation();
		waitingUserResponseDto.gender = profile.isGender();
		waitingUserResponseDto.nickname = profile.getNickname();
		waitingUserResponseDto.schoolName = member.getSchool().getName();
		waitingUserResponseDto.departmentName = member.getDepartment();
		waitingUserResponseDto.studentNumber = member.getStudentNumber();
		return waitingUserResponseDto;
	}
}
