package kr.co.wingle.member.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.member.MemberRepository;
import kr.co.wingle.member.dto.MemoRequestDto;
import kr.co.wingle.member.dto.MemoResponseDto;
import kr.co.wingle.member.dto.RejectionRequestDto;
import kr.co.wingle.member.dto.RejectionResponseDto;
import kr.co.wingle.member.dto.SignupListResponseDto;
import kr.co.wingle.member.dto.WaitingUserResponseDto;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.entity.Permission;
import kr.co.wingle.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;
	private final int pageSize = 10;

	@Transactional(readOnly = true)
	public List<SignupListResponseDto> getWaitingList(int page) {
		PageRequest pageRequest = PageRequest.of(page, pageSize);

		return memberRepository.findAllByPermissionOrderByCreatedTimeDesc(Permission.WAIT.getStatus(), pageRequest)
			.stream().map(member -> {
				String nation = profileRepository.findNationByMember(member);
				return SignupListResponseDto.from(member, nation);
			}).toList();
	}

	@Transactional(readOnly = true)
	public List<SignupListResponseDto> getRejectionList(int page) {
		PageRequest pageRequest = PageRequest.of(page, pageSize);

		return memberRepository.findAllByPermissionOrderByCreatedTimeDesc(Permission.DENY.getStatus(), pageRequest)
			.stream().map(member -> {
				String nation = profileRepository.findNationByMember(member);
				return SignupListResponseDto.from(member, nation);
			}).toList();
	}

	@Transactional(readOnly = true)
	public List<SignupListResponseDto> getAcceptanceList(int page) {
		PageRequest pageRequest = PageRequest.of(page, pageSize);

		return memberRepository.findAllByPermissionOrderByCreatedTimeDesc(Permission.APPROVE.getStatus(), pageRequest)
			.stream().map(member -> {
				String nation = profileRepository.findNationByMember(member);
				return SignupListResponseDto.from(member, nation);
			}).toList();
	}

	@Transactional(readOnly = true)
	public WaitingUserResponseDto getWaitingUserInfo(Long userId) {
		Member member = findMemberByUserId(userId);
		String nation = profileRepository.findNationByMember(member);
		return WaitingUserResponseDto.from(member, nation);
	}

	@Transactional
	public MemoResponseDto saveMemo(MemoRequestDto memoRequestDto) {
		Member member = findMemberByUserId(memoRequestDto.getUserId());
		member.setMemo(memoRequestDto.getMemo());
		return MemoResponseDto.from(memoRequestDto.getMemo());
	}

	@Transactional
	public RejectionResponseDto saveRejectionReason(RejectionRequestDto request) {
		Member member = findMemberByUserId(request.getUserId());
		member.setRejectionReason(request.getReason());
		return RejectionResponseDto.from(request.getReason());
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

	public Member findMemberByUserId(Long userId) {
		return memberRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}
}
