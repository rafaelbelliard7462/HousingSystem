package com.example.individualproject.persistance;

import com.example.individualproject.persistance.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RefreshTokenEntity r WHERE r.user.email = :email")
    boolean existsByEmail( String email);
    Optional<RefreshTokenEntity> findByUserId(Long userId);
}
