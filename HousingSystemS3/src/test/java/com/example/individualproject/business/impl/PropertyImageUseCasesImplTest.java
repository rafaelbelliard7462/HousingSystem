package com.example.individualproject.business.impl;

import com.example.individualproject.business.ImageServiceUseCase;
import com.example.individualproject.domain.CreatePropertyImageRequest;
import com.example.individualproject.domain.CreatePropertyImageResponse;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.PropertyImageRepository;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.PropertyImageEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyImageUseCasesImplTest {
    @Mock
    private PropertyImageRepository propertyImageRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private ImageServiceUseCase imageServiceUseCase;
    @InjectMocks
    private PropertyImageUseCasesImpl propertyImageUseCases;
    PropertyEntity CreatePropertyEntity(){
        UserEntity user = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .build();

        AddressEntity address = AddressEntity.builder()
                .id(1L)
                .street("Molenslag 62")
                .city("Breda")
                .postCode("4817GZ")
                .build();
        return PropertyEntity.builder()
                .id(1L)
                .user(user)
                .propertyType(PropertyType.ROOM)
                .address(address)
                .price(450.00)
                .description("Some description")
                .available( new Date())
                .room(1)
                .propertyImages(new ArrayList<>())
                .build();


    }
    @Test
    void createPropertyImage_shouldBeEqual_whenAddingPropertyImage() throws Exception {
        CreatePropertyImageRequest request = CreatePropertyImageRequest.builder()
                .imageName("imageName")
                .propertyId(1L)
                .multipartFile(createMockMultipartFile("test-image.jpg", "your_modified_content".getBytes()))
                .build();
        PropertyEntity propertyEntity = CreatePropertyEntity();
        String imageUrl = "url";
        String fileName = "filename";

        PropertyImageEntity propertyImageEntity = PropertyImageEntity.builder()
                .id(3L)
                .property(propertyEntity)
                .imageName(request.getImageName())
                .imageUrl(imageUrl)
                .fileName(fileName)
                .build();

        when(propertyRepository.findById(request.getPropertyId()))
                .thenReturn(Optional.of(propertyEntity));

        when(imageServiceUseCase.upload(request.getMultipartFile()))
                .thenReturn(Map.of(imageUrl, fileName));
        when(propertyImageRepository.save(any(PropertyImageEntity.class)))
                .thenAnswer(invocation -> {
                    PropertyImageEntity savedEntity = invocation.getArgument(0);
                    savedEntity.setId(3L);
                    return savedEntity;
                });


        CreatePropertyImageResponse actualResult = propertyImageUseCases.createPropertyImage(request);

        CreatePropertyImageResponse expectedResult = CreatePropertyImageResponse.builder()
                .propertyImageId(propertyImageEntity.getId())
                .build();
        assertEquals(expectedResult, actualResult);

        verify(propertyRepository).findById(request.getPropertyId());
        verify(imageServiceUseCase).upload(request.getMultipartFile());
        verify(propertyImageRepository).save(any(PropertyImageEntity.class));
    }

    @Test
    void deletePropertyImage_shouldBeVerified_whenDeletingByPropertyImageId() {
        PropertyEntity propertyEntity = CreatePropertyEntity();
        String imageUrl = "url";
        String fileName = "filename";
        PropertyImageEntity propertyImageEntity = PropertyImageEntity.builder()
                .id(3L)
                .property(propertyEntity)
                .imageName("imageName")
                .imageUrl(imageUrl)
                .fileName(fileName)
                .build();
        when(propertyImageRepository.findById(3L))
                .thenReturn(Optional.of(propertyImageEntity));
        when(imageServiceUseCase.deleteImage(propertyImageEntity.getFileName()))
                .thenReturn(true);

        propertyImageUseCases.deletePropertyImage(3L);
        verify(propertyImageRepository).deleteById(propertyImageEntity.getId());
    }
    @Test
    void deletePropertyImage_shouldBeNotVerified_whenDeletingImageFromCloudIsFalse() {
        PropertyEntity propertyEntity = CreatePropertyEntity();
        String imageUrl = "url";
        String fileName = "filename";
        PropertyImageEntity propertyImageEntity = PropertyImageEntity.builder()
                .id(3L)
                .property(propertyEntity)
                .imageName("imageName")
                .imageUrl(imageUrl)
                .fileName(fileName)
                .build();
        when(propertyImageRepository.findById(3L))
                .thenReturn(Optional.of(propertyImageEntity));
        when(imageServiceUseCase.deleteImage(propertyImageEntity.getFileName()))
                .thenReturn(false);

        propertyImageUseCases.deletePropertyImage(3L);
        verify(propertyImageRepository, never()).deleteById(propertyImageEntity.getId());
    }

    private MockMultipartFile createMockMultipartFile(String fileName, byte[] content) throws Exception {
        return new MockMultipartFile("file", fileName, "image/jpeg", content);
    }

}
