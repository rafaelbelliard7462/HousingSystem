package com.example.individualproject.business.impl;

import com.example.individualproject.business.PropertyStatisticsUseCase;
import com.example.individualproject.domain.PropertyStatistics;
import com.example.individualproject.persistance.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class PropertyStatisticsUseCaseImpl implements PropertyStatisticsUseCase {
    private  final PropertyRepository propertyRepository;
    @Override
    public List<PropertyStatistics> getTotalPricesByMonthAndYear(int year) {
        return propertyRepository.getTotalPricesByMonthAndYear(year);
    }
}
