package kr.co.wingle.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.member.entity.TermMember;

public interface TermMemberRepository extends JpaRepository<TermMember, Long> {
	List<TermMember> findAllByMember(Member member);
}
