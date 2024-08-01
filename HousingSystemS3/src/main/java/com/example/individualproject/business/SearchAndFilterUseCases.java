package com.example.individualproject.business;

import com.example.individualproject.domain.Application;
import com.example.individualproject.domain.Property;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchAndFilterUseCases {
    Page<Application> getApplicationsByUser(Long userId, Status status, Pageable pageable);
    Page<Application> searchApplicationsFromUser(Long userId, Status status, String searchString, Pageable pageable);

    Page<Property> searchProperties(String city, double maxPrice, int minSize, PropertyType propertyType, boolean rented, Pageable pageable);
    Page<Property> searchHomeownerProperties(String city, double maxPrice, int minSize, PropertyType propertyType, boolean rented, Long userId,Pageable pageable);
}
