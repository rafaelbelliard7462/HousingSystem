package com.example.individualproject.persistance;

import com.example.individualproject.persistance.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    boolean existsByHomeowner_IdAndHomeSeeker_Id(Long homeownerId, Long homeSeekerId);
    Optional<ChatEntity> findByHomeowner_IdAndHomeSeeker_Id(Long homeownerId, Long homeSeekerId);
    List<ChatEntity> findAllByHomeSeeker_Id(Long user1Id);
    List<ChatEntity> findAllByHomeowner_Id(Long user2Id);
}
