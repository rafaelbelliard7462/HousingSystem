package com.example.individualproject.business.impl;

import com.example.individualproject.business.ImageServiceUseCase;
import com.example.individualproject.business.PropertyImageUseCases;
import com.example.individualproject.domain.CreatePropertyImageRequest;
import com.example.individualproject.domain.CreatePropertyImageResponse;
import com.example.individualproject.persistance.PropertyImageRepository;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.PropertyImageEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PropertyImageUseCasesImpl implements PropertyImageUseCases {
    private final PropertyImageRepository propertyImageRepository;
    private  final PropertyRepository propertyRepository;
    private final ImageServiceUseCase imageServiceUseCase;
    @Override
    public CreatePropertyImageResponse createPropertyImage(CreatePropertyImageRequest request) {
        PropertyImageEntity propertyImageEntity= saveNewPropertyImage(request);
        return CreatePropertyImageResponse.builder().propertyImageId(propertyImageEntity.getId()).build();
    }

    @Override
    public void deletePropertyImage(Long propertyImage) {
        PropertyImageEntity propertyImageEntity= propertyImageRepository.findById(propertyImage).orElseThrow();
        if(imageServiceUseCase.deleteImage(propertyImageEntity.getFileName())){
            propertyImageRepository.deleteById(propertyImageEntity.getId());
        }
    }

    private PropertyImageEntity saveNewPropertyImage(CreatePropertyImageRequest request) {
        PropertyEntity propertyEntity = propertyRepository.findById(request.getPropertyId()).orElseThrow();
        Map<String, String> imageUploadResult = imageServiceUseCase.upload(request.getMultipartFile());

        String imageUrl = imageUploadResult.get("url");
        String fileName = imageUploadResult.get("filename");

        PropertyImageEntity propertyImageEntity = PropertyImageEntity.builder()
                .property(propertyEntity)
                .imageName(request.getImageName())
                .imageUrl(imageUrl)
                .fileName(fileName)
                .build();

        propertyEntity.getPropertyImages().add(propertyImageEntity);

        // Save the new PropertyImageEntity
        propertyImageRepository.save(propertyImageEntity);

        // Return the created PropertyImageEntity
        return propertyImageEntity;
    }


}
