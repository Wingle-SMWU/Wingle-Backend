package kr.co.wingle.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.wingle.member.entity.Member;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	@Query("select p from Profile p where p.member = :member")
	String findNationByMember(@Param("member") Member member);

	Optional<Profile> findByMember(Member member);
}
