package com.example.individualproject.persistance;

import com.example.individualproject.persistance.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByChat_Id(Long chatId);
}
