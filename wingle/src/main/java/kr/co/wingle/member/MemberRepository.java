package kr.co.wingle.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);
}
