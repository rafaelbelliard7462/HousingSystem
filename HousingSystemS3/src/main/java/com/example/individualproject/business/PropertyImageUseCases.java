package com.example.individualproject.business;

import com.example.individualproject.domain.CreatePropertyImageRequest;
import com.example.individualproject.domain.CreatePropertyImageResponse;

public interface PropertyImageUseCases {
    CreatePropertyImageResponse createPropertyImage(CreatePropertyImageRequest request);
    void deletePropertyImage(Long propertyImage);
}
