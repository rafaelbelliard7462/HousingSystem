package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePropertyRequest {
    private Long id;
    @NotNull
    private  Long userId;
    @NotNull
    private Address address;
    @NotNull
    private PropertyType propertyType;
    @NotNull
    private double price;
    @NotNull
    private int room;
    @NotNull
    private Date available;
    @NotBlank
    private String description;
    @NotNull
    private boolean rented;

    private Date rentedDate;

}
