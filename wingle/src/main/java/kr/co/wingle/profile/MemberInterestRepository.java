package kr.co.wingle.profile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.profile.entity.MemberInterest;

public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {
	List<MemberInterest> findAllByMember(Member member);
}
