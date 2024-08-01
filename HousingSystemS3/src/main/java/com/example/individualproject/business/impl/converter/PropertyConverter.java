package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.Property;
import com.example.individualproject.domain.User;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;


public class PropertyConverter {
private PropertyConverter(){}
    public static User userConvert(UserEntity userEntity) {
        return  User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .dateOfBirth(userEntity.getDateOfBirth())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }
    public static Property convert(PropertyEntity property) {
        return  Property.builder()
                .id(property.getId())
                .user(userConvert(property.getUser()))
                .price(property.getPrice())
                .size(property.getSize())
                .address(AddressConverter.convert(property.getAddress()))
                .room(property.getRoom())
                .available(property.getAvailable())
                .propertyType(property.getPropertyType())
                .description(property.getDescription())
                .rented(property.isRented())
                .rentedDate(property.getRentedDate())
                .applications(property.getApplications().stream().map(ApplicationConverter ::convert).toList())
                .propertyImages(property.getPropertyImages().stream().map(PropertyImageConverter ::convert).toList())
                .build();
    }
}
