package kr.co.wingle.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.message.entity.RoomMember;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
}