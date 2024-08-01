package com.example.individualproject.persistance;

import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.persistance.entity.PreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {
    @Query("SELECT p FROM PreferenceEntity p WHERE p.user.id = :userId")
    Optional<PreferenceEntity> findByUserId(Long userId);

    @Query("SELECT p FROM PreferenceEntity p WHERE " +
            "(p.city = :city) AND " +
            "(p.propertyType = :propertyType) AND " +
            "(p.price <= :price) AND " +
            "(p.room = :room)")
    List<PreferenceEntity> findByCityAndAndPropertyTypeAndPriceAndRoom(
            @Param("city") String city,
            @Param("propertyType") PropertyType propertyType,
            @Param("price") Double price,
            @Param("room") Integer room);


}
