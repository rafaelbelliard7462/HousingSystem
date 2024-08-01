package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.PropertyType;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Property {
    private Long id;
    private  User user;
    private Address address;
    private PropertyType propertyType;
    private double price;
    private int size;
    private int room;
    private Date available;
    private String description;
    private boolean rented;
    private Date rentedDate;
    @Builder.Default
    private List<Application> applications = new ArrayList<>();
    @Builder.Default
    private List<PropertyImage> propertyImages = new ArrayList<>();

}
