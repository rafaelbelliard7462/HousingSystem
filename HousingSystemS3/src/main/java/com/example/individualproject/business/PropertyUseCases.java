package com.example.individualproject.business;

import com.example.individualproject.domain.*;

import java.util.Optional;

public interface PropertyUseCases {

    CreatePropertyResponse createProperty (CreatePropertyRequest request);
    GetPropertiesResponse getProperties();
    GetPropertiesResponse getPropertiesByUser(Long userId);
    Optional<Property> getPropertyById(Long propertyId);
    void updateProperty (UpdatePropertyRequest request);
    void deleteProperty (Long propertyId);

}
