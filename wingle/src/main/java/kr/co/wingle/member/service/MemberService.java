package kr.co.wingle.member.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.WaitingListResponseDto;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.entity.Permission;
import kr.co.wingle.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	public List<WaitingListResponseDto> getWaitingList(int page) {
		PageRequest pageRequest = PageRequest.of(page, 15);

		return memberRepository.findAllByPermissionOrderByCreatedTimeDesc(Permission.WAIT.getStatus(), pageRequest)
			.stream().map(member -> {
				String nation = profileRepository.findNationByMember(member);
				return WaitingListResponseDto.of(member.getId(), member.getCreatedTime(), member.getName(), nation);
			}).toList();
	}

	public boolean validate(long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		if (member.isDeleted() == true) {
			throw new IllegalArgumentException(ErrorCode.NO_ID.getMessage());
		}
		// TODO: 관리자페이지에서 승인 받은 회원인지 검사
		return true;
	}
}
