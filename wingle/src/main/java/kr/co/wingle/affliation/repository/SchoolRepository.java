package kr.co.wingle.affliation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.affliation.entity.School;

public interface SchoolRepository extends JpaRepository<School, Long> {
	Optional<School> findByCode(String code);
}
