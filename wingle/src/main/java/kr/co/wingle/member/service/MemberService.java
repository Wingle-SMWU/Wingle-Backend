package kr.co.wingle.member.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.WaitingListResponseDto;
import kr.co.wingle.member.entity.Permiss
ion;
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
