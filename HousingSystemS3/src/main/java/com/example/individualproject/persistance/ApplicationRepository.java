package com.example.individualproject.persistance;

import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long>, JpaSpecificationExecutor<ApplicationEntity> {
    boolean existsByUser_IdAndProperty_Id(Long userId, Long propertyId);

    Page<ApplicationEntity> findByProperty_User_IdAndStatus(Long userId, Status status, Pageable pageable);

    @Query("SELECT a FROM ApplicationEntity a WHERE a.user.id = :userId")
    List<ApplicationEntity> findAllByUserId(Long userId);

    @Query("SELECT a FROM ApplicationEntity a WHERE a.property.id = :propertyId")
    List<ApplicationEntity> findAllByPropertyId(Long propertyId);
}
