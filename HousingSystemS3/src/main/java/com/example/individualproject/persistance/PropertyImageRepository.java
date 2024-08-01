package com.example.individualproject.persistance;

import com.example.individualproject.persistance.entity.PropertyImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyImageRepository extends JpaRepository<PropertyImageEntity, Long> {
}
