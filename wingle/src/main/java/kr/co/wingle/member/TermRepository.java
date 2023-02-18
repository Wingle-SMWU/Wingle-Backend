package kr.co.wingle.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.member.entity.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
	Optional<Term> findByName(String name);
}
