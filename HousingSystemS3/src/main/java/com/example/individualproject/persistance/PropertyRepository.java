package com.example.individualproject.persistance;

import com.example.individualproject.domain.PropertyStatistics;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.persistance.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyRepository extends JpaRepository<PropertyEntity,Long> {
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PropertyEntity p WHERE p.address.street = :street")
    boolean existsByStreet( String street);

    Page<PropertyEntity> findAllByRented(boolean rented, Pageable pageable);
    Page<PropertyEntity> findAllByRentedAndUser_Id(boolean rented, Long userId,Pageable pageable);
    @Query("SELECT p FROM PropertyEntity p WHERE p.user.id = :userId")
    List<PropertyEntity> findAllByUserId(Long userId);
    @Query("SELECT p FROM PropertyEntity p WHERE " +
            "(:city IS NULL OR p.address.city = :city) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:minSize IS NULL OR p.size >= :minSize) AND " +
            "(:propertyType IS NULL OR p.propertyType = :propertyType) AND " +
            "(:rented IS NULL OR p.rented = :rented)")
    Page<PropertyEntity> findByCityAndPriceAndSize(
            @Param("city") String city,
            @Param("maxPrice") Double maxPrice,
            @Param("minSize") Integer minSize,
            @Param("propertyType") PropertyType propertyType,
            @Param("rented") boolean rented,
            Pageable pageable);
    @Query("SELECT p FROM PropertyEntity p WHERE " +
            "(:city IS NULL OR p.address.city = :city) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:minSize IS NULL OR p.size >= :minSize) AND " +
            "(:propertyType IS NULL OR p.propertyType = :propertyType) AND " +
            "(:rented IS NULL OR p.rented = :rented) AND " +
            "( p.user.id = :userId)  " )
    Page<PropertyEntity> findByCityAndPriceAndSizeAndUserId(
            @Param("city") String city,
            @Param("maxPrice") Double maxPrice,
            @Param("minSize") Integer minSize,
            @Param("propertyType") PropertyType propertyType,
            @Param("rented") boolean rented,
            @Param("userId") Long userId,
            Pageable pageable);


    @Query("SELECT NEW com.example.individualproject.domain.PropertyStatistics(" +
            "MONTH(p.rentedDate), YEAR(p.rentedDate), SUM(p.price)) " +
            "FROM PropertyEntity p " +
            "WHERE YEAR(p.rentedDate) = :year AND p.rented = true " +
            "GROUP BY MONTH(p.rentedDate), YEAR(p.rentedDate)")
    List<PropertyStatistics> getTotalPricesByMonthAndYear(@Param("year") int year);





}
