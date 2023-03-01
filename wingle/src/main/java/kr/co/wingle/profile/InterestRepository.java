package kr.co.wingle.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.profile.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {
	Optional<Interest> findByName(String name);
}
