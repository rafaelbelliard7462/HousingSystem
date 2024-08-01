package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.Preference;
import com.example.individualproject.persistance.entity.PreferenceEntity;

public class PreferenceConverter {
    private PreferenceConverter(){}
    public static Preference convert(PreferenceEntity preference) {
        return  Preference.builder()
                .id(preference.getId())
                .user(UserConverter.convert(preference.getUser()))
                .price(preference.getPrice())
                .city(preference.getCity())
                .room(preference.getRoom())
                .propertyType(preference.getPropertyType())
                .build();
    }
}
