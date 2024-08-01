package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.PropertyImage;
import com.example.individualproject.persistance.entity.PropertyImageEntity;

public class PropertyImageConverter {
    private PropertyImageConverter(){}
    public static PropertyImage convert(PropertyImageEntity propertyImage) {
        return  PropertyImage.builder()
                .id(propertyImage.getId())
                .property(ApplicationConverter.propertyConvert(propertyImage.getProperty()))
                .imageName(propertyImage.getImageName())
                .imageUrl(propertyImage.getImageUrl())
                .fileName(propertyImage.getFileName())
                .build();
    }
}
