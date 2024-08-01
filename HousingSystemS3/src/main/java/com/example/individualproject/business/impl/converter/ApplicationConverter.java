package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.Application;
import com.example.individualproject.domain.Property;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;


public class ApplicationConverter {
    private ApplicationConverter(){

    }

    public static Property propertyConvert(PropertyEntity property) {
        return  Property.builder()
                .id(property.getId())
                .user(PropertyConverter.userConvert(property.getUser()))
                .price(property.getPrice())
                .address(AddressConverter.convert(property.getAddress()))
                .room(property.getRoom())
                .available(property.getAvailable())
                .propertyType(property.getPropertyType())
                .description(property.getDescription())
                .rented(property.isRented())
                .rentedDate(property.getRentedDate())
                .build();
    }
    public static Application convert(ApplicationEntity application) {
        return  Application.builder()
                .id(application.getId())
                .user(PropertyConverter.userConvert(application.getUser()))
                .property(propertyConvert(application.getProperty()))
                .appliedDate(application.getAppliedDate())
                .status(application.getStatus())
                .description(application.getDescription())
                .build();
    }

}
