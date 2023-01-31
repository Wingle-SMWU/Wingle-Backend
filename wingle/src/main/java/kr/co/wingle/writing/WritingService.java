package kr.co.wingle.writing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.member.Member;
import kr.co.wingle.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WritingService {
	private final WritingRepository writingRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public WritingDto create(Long memberId, String content) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		Writing writing = Writing.createWriting(member, content);
		Writing savedWriting = writingRepository.save(writing);
		WritingDto writingDto = WritingDto.fromEntity(savedWriting);
		return writingDto;
	}

	@Transactional
	public WritingDto softDelete(Long memberId, Long writingId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		Writing writing = writingRepository.findById(writingId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		if (writing.getMember().getId() != memberId) {
			throw new IllegalArgumentException(ErrorCode.NO_ACCESS.getMessage());
		}
		writing.softDelete();
		writing = writingRepository.save(writing);
		WritingDto writingDto = WritingDto.fromEntity(writing);
		return writingDto;
	}
}
