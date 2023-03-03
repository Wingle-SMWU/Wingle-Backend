package kr.co.wingle.message.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByRoomIdAndIsDeleted(Long roomId, boolean isDeleted, Pageable pageable);

}