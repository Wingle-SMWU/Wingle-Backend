package kr.co.wingle.message;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.message.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}