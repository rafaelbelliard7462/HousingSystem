package com.example.individualproject.controller;

import com.example.individualproject.business.SearchAndFilterUseCases;
import com.example.individualproject.domain.Application;
import com.example.individualproject.domain.Property;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/searchAndFilter")
@AllArgsConstructor
public class SearchAndFilterController {
    private  final SearchAndFilterUseCases searchAndFilterUseCases;
    @GetMapping("/findApplicationsByHomeowner/{userId}")
    public ResponseEntity<Page<Application>> getApplicationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int itemsNum,
            @RequestParam Status status) {
        Pageable pageable = PageRequest.of(pageNum, itemsNum);
        Page<Application> applications = searchAndFilterUseCases.getApplicationsByUser(userId, status, pageable);
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/search/{userId}")
    public ResponseEntity<Page<Application>> searchApplicationsFromUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int itemsNum,
            @RequestParam Status status,
            @RequestParam String searchString) {
        Pageable pageable = PageRequest.of(pageNum, itemsNum);
        Page<Application> applications = searchAndFilterUseCases.searchApplicationsFromUser(userId, status, searchString, pageable);
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<Property>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minSize,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) boolean rented,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "6") int itemsNum) {
        Pageable pageable = PageRequest.of(pageNum, itemsNum);
        Page<Property> page = searchAndFilterUseCases.searchProperties(city, maxPrice, minSize, propertyType,rented,pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/searchHomeowner/{id}")
    public ResponseEntity<Page<Property>> searchHomeownerProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minSize,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) boolean rented,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "6") int itemsNum,
            @PathVariable(value = "id") Long id) {
        Pageable pageable = PageRequest.of(pageNum, itemsNum);
        Page<Property> page = searchAndFilterUseCases.searchHomeownerProperties(city, maxPrice, minSize, propertyType,rented, id,pageable);
        return ResponseEntity.ok(page);
    }
}
