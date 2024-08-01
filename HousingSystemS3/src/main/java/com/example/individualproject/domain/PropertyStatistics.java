package com.example.individualproject.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class PropertyStatistics {
    private int month;
    private int year;
    private double totalMonthlyPrice;

}