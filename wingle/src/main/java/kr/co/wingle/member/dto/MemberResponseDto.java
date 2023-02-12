package kr.co.wingle.member.dto;

import kr.co.wingle.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
	private Long id;
	private String email;
	private String name;

	public static MemberResponseDto from(Member member) {
		MemberResponseDto memberResponseDto = new MemberResponseDto();
		memberResponseDto.id = member.getId();
		memberResponseDto.email = member.getEmail();
		memberResponseDto.name = member.getName();
		return memberResponseDto;
	}
}
