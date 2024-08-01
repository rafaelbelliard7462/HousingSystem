package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.User;
import com.example.individualproject.persistance.entity.UserEntity;


public class UserConverter {
private  UserConverter(){}

    public static User convert(UserEntity userEntity) {
        return  User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .dateOfBirth(userEntity.getDateOfBirth())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .properties(userEntity.getProperties().stream().map(PropertyConverter ::convert).toList())
                .applications(userEntity.getApplications().stream().map(ApplicationConverter ::convert).toList())
                .build();
    }
}
