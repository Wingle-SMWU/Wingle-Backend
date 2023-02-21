package kr.co.wingle.message;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.wingle.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}