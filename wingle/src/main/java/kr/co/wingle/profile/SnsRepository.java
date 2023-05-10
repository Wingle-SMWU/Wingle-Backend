package kr.co.wingle.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.profile.entity.Sns;

public interface SnsRepository extends JpaRepository<Sns, Long> {
	String findAllByMember(Member member);
}
