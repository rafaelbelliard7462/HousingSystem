package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.PropertyType;
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
public class CreatePropertyRequest {
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
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private Date available;
    @NotNull
    private String description;
    @NotNull
    private boolean rented;

    private Date rentedDate;
}
