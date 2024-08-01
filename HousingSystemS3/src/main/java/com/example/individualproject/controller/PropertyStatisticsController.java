package com.example.individualproject.controller;

import com.example.individualproject.business.PropertyStatisticsUseCase;
import com.example.individualproject.domain.PropertyStatistics;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/propertyStatistics")
@AllArgsConstructor
public class PropertyStatisticsController {

    private  final PropertyStatisticsUseCase propertyStatisticsUseCase;
    @GetMapping("/{year}")
    public ResponseEntity<List<PropertyStatistics>> getTotalPricesByMonthAndYear(@PathVariable(value = "year") int year){
        return ResponseEntity.ok().body(propertyStatisticsUseCase.getTotalPricesByMonthAndYear(year));
    }
}
