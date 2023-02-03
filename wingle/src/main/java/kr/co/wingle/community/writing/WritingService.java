package kr.co.wingle.community.writing;

import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.member.MemberService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class WritingService {
	private MemberService memberService;

	@Transactional
	public WritingDto softDelete(Long memberId, Long writingId, WritingRepository repository) {
		memberService.validate(memberId);
		Writing writing = repository.findById(writingId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		if (writing.getMember().getId() != memberId) {
			throw new IllegalArgumentException(ErrorCode.NO_ACCESS.getMessage());
		}
		writing.softDelete();
		writing = repository.save(writing);
		WritingDto writingDto = WritingDto.fromEntity(writing);
		return writingDto;
	}
}
