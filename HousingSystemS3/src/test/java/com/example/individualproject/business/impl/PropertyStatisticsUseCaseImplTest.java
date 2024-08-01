package com.example.individualproject.business.impl;

import com.example.individualproject.domain.PropertyStatistics;
import com.example.individualproject.persistance.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyStatisticsUseCaseImplTest {
    @Mock
    private  PropertyRepository propertyRepository;
    @InjectMocks
    private  PropertyStatisticsUseCaseImpl propertyStatisticsUseCase;



    @Test
    void getTotalPricesByMonthAndYear_shouldBeEqual_whenGettingPropertyStatistics() {
        PropertyStatistics propertyStatistics= PropertyStatistics.builder()
                .year(2024)
                .month(1)
                .totalMonthlyPrice(500.0)
                .build();


        when(propertyRepository.getTotalPricesByMonthAndYear(2024))
                .thenReturn(List.of(propertyStatistics));

        List<PropertyStatistics>  actualResult = propertyStatisticsUseCase.getTotalPricesByMonthAndYear(2024);
        List<PropertyStatistics>  expectedResult = List.of(propertyStatistics);

        assertEquals(expectedResult, actualResult);
        verify(propertyRepository).getTotalPricesByMonthAndYear(2024);
    }
}