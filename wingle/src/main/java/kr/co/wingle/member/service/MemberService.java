package kr.co.wingle.member.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.common.util.AES256Util;
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
	private final AES256Util aes;
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
		Member member = findMemberByMemberId(userId);
		String nation = profileRepository.findNationByMember(member);
		return WaitingUserResponseDto.from(member, nation);
	}

	@Transactional
	public MemoResponseDto saveMemo(MemoRequestDto memoRequestDto) {

		Member member = findMemberByMemberId(Long.parseLong(aes.decrypt(memoRequestDto.getUserId())));
		member.setMemo(memoRequestDto.getMemo());
		return MemoResponseDto.from(memoRequestDto.getMemo());
	}

	@Transactional
	public RejectionResponseDto saveRejectionReason(RejectionRequestDto request) {
		Member member = findMemberByMemberId(Long.parseLong(aes.decrypt(request.getUserId())));
		member.setRejectionReason(request.getReason());
		return RejectionResponseDto.from(request.getReason());
	}

	public boolean isAcceptedMember(Long memberId) {
		Member member = findMemberByMemberId(memberId);
		if (member.isDeleted() == true) {
			throw new NotFoundException(ErrorCode.ALREADY_WITHDRAWN);
		}
		// 관리자페이지에서 회원가입 승인 받은 회원인지 or 관리자인지 검사
		if (member.getPermission() == Permission.WAIT.getStatus()) {
			throw new ForbiddenException(ErrorCode.NOT_ACCEPTED);
		}
		return true;
	}

	public Member findMemberByMemberId(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}

	public Member findAcceptedMemberByMemberId(Long memberId) {
		if (!isAcceptedMember(memberId)) {
			throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
		}
		return findMemberByMemberId(memberId);
	}

	public long getTotalPages(int permission, int page) {
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		Page<Member> pages = memberRepository.findAllByPermission(permission, pageRequest);
		return pages.getTotalPages();
	}
}
