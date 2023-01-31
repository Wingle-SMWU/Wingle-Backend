package kr.co.wingle.writing;

import org.springframework.stereotype.Service;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.member.Member;
import kr.co.wingle.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WritingService {

	private final WritingRepository writingRepository;
	private final MemberRepository memberRepository;

	public WritingDto create(Long memberId, String content) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NO_ID.getMessage()));

		Writing writing = Writing.createWriting(member, content);
		Writing savedWriting = writingRepository.save(writing);
		WritingDto writingDto = WritingDto.fromEntity(savedWriting);
		return writingDto;
	}
}
