package com.example.individualproject.business;

import com.example.individualproject.domain.PropertyStatistics;

import java.util.List;

public interface PropertyStatisticsUseCase {

    List<PropertyStatistics> getTotalPricesByMonthAndYear(int year);
}
