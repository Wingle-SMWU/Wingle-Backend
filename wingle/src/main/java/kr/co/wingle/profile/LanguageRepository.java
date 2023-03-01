package kr.co.wingle.profile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.member.entity.Member;
import kr.co.wingle.profile.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
	List<Language> findAllByMemberOrderByOrderNumberAsc(Member member);

	List<Language> findAllByMember(Member member);
}
