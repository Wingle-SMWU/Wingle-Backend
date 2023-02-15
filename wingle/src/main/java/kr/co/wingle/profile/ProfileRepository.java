package kr.co.wingle.profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	boolean existsByNickname(String nickname);
}
