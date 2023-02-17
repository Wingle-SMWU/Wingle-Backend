package kr.co.wingle.member.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.CustomException;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.WaitingListResponseDto;
import kr.co.wingle.member.entity.Authority;
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
}
