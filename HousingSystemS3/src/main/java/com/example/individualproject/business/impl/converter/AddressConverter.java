package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.Address;
import com.example.individualproject.persistance.entity.AddressEntity;


public class AddressConverter {
    private AddressConverter(){

    }

    public static Address convert(AddressEntity address) {
        return  Address.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .postCode(address.getPostCode())
                .build();
    }
}
