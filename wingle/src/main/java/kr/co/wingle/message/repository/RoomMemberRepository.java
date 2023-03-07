package kr.co.wingle.message.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.message.entity.RoomMember;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
	List<RoomMember> findAllByMemberIdAndIsDeleted(Long memberId, boolean isDeleted);

	List<RoomMember> findByMemberIdAndIsDeleted(Long memberId, boolean isDeleted, Pageable pageable);

	List<RoomMember> findAllByRoomIdAndIsDeleted(Long roomId, boolean isDeleted);

	Optional<RoomMember> findByRoomIdAndMemberIdAndIsDeleted(Long roomId, Long memberId, boolean isDeleted);
}