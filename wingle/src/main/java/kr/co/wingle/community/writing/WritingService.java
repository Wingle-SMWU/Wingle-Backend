package kr.co.wingle.community.writing;

import org.springframework.transaction.annotation.Transactional;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.ForbiddenException;
import kr.co.wingle.common.exception.NotFoundException;
import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.service.MemberService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class WritingService {
	private MemberService memberService;

	@Transactional
	protected WritingDto softDelete(Long memberId, Long writingId, WritingRepository repository) {
		memberService.validate(memberId);
		Writing writing = repository.findById(writingId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));
		if (writing.getMember().getId() != memberId) {
			throw new IllegalArgumentException(ErrorCode.FORBIDDEN_USER.getMessage());
		}
		writing.softDelete();
		writing = repository.save(writing);
		WritingDto writingDto = WritingDto.fromEntity(writing);
		return writingDto;
	}

	@Transactional(readOnly = true)
	protected boolean isValidMember(Writing writing, Member member) {
		// 작성자 다르면 에러
		if (writing.getMember().getId() != member.getId()) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN_USER);
		}
		return true;
	}

	@Transactional(readOnly = true)
	protected boolean isExist(Writing writing) {
		// 이미 삭제됐으면 에러
		if (writing.isDeleted()) {
			throw new NotFoundException(ErrorCode.ALREADY_DELETED);
		}
		return true;
	}
}
