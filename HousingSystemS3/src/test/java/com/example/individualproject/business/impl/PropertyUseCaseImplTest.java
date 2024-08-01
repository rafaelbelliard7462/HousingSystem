package com.example.individualproject.business.impl;

import com.example.individualproject.business.PropertyAlertUseCases;
import com.example.individualproject.business.exception.AlreadyExistException;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PropertyAlertUseCases propertyAlertUseCases;
    @InjectMocks
    private PropertyUseCasesImpl propertyUseCase;

    CreatePropertyRequest CreatePropertyObject(){

        Address address = Address.builder()
                .id(1L)
                .street("Molenslag 62")
                .city("Breda")
                .postCode("4817GZ")
                .build();
       return CreatePropertyRequest.builder()
                .userId(1L)
                .propertyType(PropertyType.ROOM)
                .address(address)
                .price(450.00)
                .description("Some description")
                .available( new Date())
                .room(1)
                .build();


    }
    User createUser(){
        return  User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(new ArrayList<>())
                .build();
    }
    public UserEntity getUserEntity(User user){

        return  UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .properties(new ArrayList<>())
                .build();
    }

    public AddressEntity getAddressEntity(Address address){
        return  AddressEntity.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .postCode(address.getPostCode())
                .build();
    }
    @Test
    void createProperty_shouldBeEqual_WhenUserTryingToAProperty() {

        CreatePropertyRequest request = CreatePropertyObject();
        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .user(getUserEntity(createUser()))
                .propertyType(request.getPropertyType())
                .address(getAddressEntity(request.getAddress()))
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .build();

        when(propertyRepository.save(any(PropertyEntity.class)))
                .thenReturn(propertyEntity);
        when(userRepository.findById(request.getUserId()))
                .thenReturn(Optional.of(propertyEntity.getUser()));
        CreatePropertyResponse actualResult = propertyUseCase.createProperty(request);


       Property property = Property.builder()
               .id(1L)
               .user(createUser())
               .propertyType(request.getPropertyType())
               .address(request.getAddress())
               .price(request.getPrice())
               .description(request.getDescription())
               .available( request.getAvailable())
               .room(request.getRoom())
               .build();
       CreatePropertyResponse expectedResult = CreatePropertyResponse.builder().propertyId(property.getId()).build();
        assertEquals(expectedResult,actualResult);
        verify(propertyRepository).save(any(PropertyEntity.class));
        verify(userRepository).findById(request.getUserId());

    }

    @Test
    void createProperty_shouldThrowAlreadyExistException_WhenUserTryingToAProperty() {

        CreatePropertyRequest request = CreatePropertyObject();
        when(propertyRepository.existsByStreet(request.getAddress().getStreet()))
                .thenReturn(true);

        assertThrows(AlreadyExistException.class,() -> propertyUseCase.createProperty(request));
        verify(propertyRepository).existsByStreet(request.getAddress().getStreet());

    }
    @Test
    void getProperties_shouldBeEqual_WhenGettingPropertiesByUser() {
        CreatePropertyRequest request = CreatePropertyObject();
        User user = createUser();
        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .user(getUserEntity(user))
                .propertyType(request.getPropertyType())
                .address(getAddressEntity(request.getAddress()))
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .applications(List.of())
                .propertyImages(List.of())
                .build();
        when(propertyRepository.findAllByUserId(request.getUserId()))
                .thenReturn(List.of(propertyEntity));


        GetPropertiesResponse actualResult = propertyUseCase.getPropertiesByUser(request.getUserId());

        Property property = Property.builder()
                .id(1L)
                .user(user)
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .applications(List.of())
                .propertyImages(List.of())
                .build();
        GetPropertiesResponse expectedResult = GetPropertiesResponse.builder()
                        .properties(List.of(property))
                        .build();
        assertEquals(expectedResult, actualResult);
        verify(propertyRepository).findAllByUserId(request.getUserId());

    }
    @Test
    void getProperties_shouldBeEqual_WhenGettingPropertiesByUser_WhenPropertiesListIsEmpty() {
        User user = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .build();
        when(propertyRepository.findAllByUserId(user.getId()))
                .thenReturn(List.of());
        GetPropertiesResponse actualResult = propertyUseCase.getPropertiesByUser(user.getId());

        GetPropertiesResponse expectedResult = GetPropertiesResponse.builder().properties(List.of()).build();
        assertEquals(expectedResult,actualResult);
        verify(propertyRepository).findAllByUserId(user.getId());

    }

    @Test
    void getProperties_shouldBeEqual_WhenGettingProperties() {
        CreatePropertyRequest request = CreatePropertyObject();
        User user = createUser();
        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .user(getUserEntity(user))
                .propertyType(request.getPropertyType())
                .address(getAddressEntity(request.getAddress()))
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .applications(List.of())
                .propertyImages(List.of())
                .build();
        when(propertyRepository.findAll())
                .thenReturn(List.of(propertyEntity));


        GetPropertiesResponse actualResult = propertyUseCase.getProperties();

        Property property = Property.builder()
                .id(1L)
                .user(user)
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .applications(List.of())
                .propertyImages(List.of())
                .build();
        GetPropertiesResponse expectedResult = GetPropertiesResponse.builder()
                .properties(List.of(property))
                .build();
        assertEquals(expectedResult, actualResult);
        verify(propertyRepository).findAll();

    }


    @Test
    void getProperties_shouldBeEqual_WhenGettingProperties_WhenPropertiesListIsEmpty() {

        when(propertyRepository.findAll())
                .thenReturn(List.of());
        GetPropertiesResponse actualResult = propertyUseCase.getProperties();

        GetPropertiesResponse expectedResult = GetPropertiesResponse.builder().properties(List.of()).build();
        assertEquals(expectedResult,actualResult);
        verify(propertyRepository).findAll();

    }

    @Test
    void getPropertyById_shouldBeEqual_WhenGettingPropertyById() {
        CreatePropertyRequest request = CreatePropertyObject();
        User user = createUser();
        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .user(getUserEntity(user))
                .propertyType(request.getPropertyType())
                .address(getAddressEntity(request.getAddress()))
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .applications(List.of())
                .propertyImages(List.of())
                .build();
        when(propertyRepository.findById(propertyEntity.getId()))
                .thenReturn(Optional.of(propertyEntity));



        Optional<Property> optionalProperty = propertyUseCase.getPropertyById(1L);
        Property actualResult = optionalProperty.orElseThrow();

        Property expectedResult = Property.builder()
                .id(1L)
                .user(user)
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .applications(List.of())
                .propertyImages(List.of())
                .build();

        assertEquals(expectedResult, actualResult);

    }
    @Test
    void getPropertyById_shouldThrowNotFountException_WhenGettingPropertyByIdThatDontExist() {

        CreatePropertyRequest request = CreatePropertyObject();
        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .user(getUserEntity(createUser()))
                .propertyType(request.getPropertyType())
                .address(getAddressEntity(request.getAddress()))
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .build();
        when(propertyRepository.findById(propertyEntity.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> propertyUseCase.getPropertyById(1L) );

        verify(propertyRepository).findById(propertyEntity.getId());
    }
    @Test
    void updateProperty_shouldBeEqual_WhenUpdatingProperty() {
        CreatePropertyRequest request = CreatePropertyObject();
        User user = createUser();

        UpdatePropertyRequest updateRequest = UpdatePropertyRequest.builder()
                .id(1L)
                .userId(request.getUserId())
                .propertyType(PropertyType.APARTMENT)
                .address(request.getAddress())
                .price(450.00)
                .description("Some description")
                .available( new Date())
                .room(1)
                .build();

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .user(getUserEntity(user))
                .propertyType(updateRequest.getPropertyType())
                .address(getAddressEntity(updateRequest.getAddress()))
                .price(updateRequest.getPrice())
                .description(updateRequest.getDescription())
                .available( updateRequest.getAvailable())
                .room(updateRequest.getRoom())
                .applications(List.of())
                .build();

        when(propertyRepository.findById(updateRequest.getId()))
                .thenReturn(Optional.of(propertyEntity));
        when(userRepository.findById(request.getUserId()))
                .thenReturn(Optional.of(propertyEntity.getUser()));

        propertyUseCase.updateProperty(updateRequest);

        verify(propertyRepository).findById(updateRequest.getId());
        verify(userRepository).findById(updateRequest.getUserId());
        verify(propertyRepository).save(propertyEntity);

    }

    @Test
    void updateProperty_shouldThrowNotFoundException_WhenUpdatingPropertyButIdDoesNotExist() {
        CreatePropertyRequest request = CreatePropertyObject();

        UpdatePropertyRequest updateRequest = UpdatePropertyRequest.builder()
                .id(2L)
                .userId(request.getUserId())
                .propertyType(PropertyType.APARTMENT)
                .address(request.getAddress())
                .price(450.00)
                .description("Some description")
                .available( new Date())
                .room(1)
                .build();
        when(propertyRepository.findById(updateRequest.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> propertyUseCase.updateProperty(updateRequest));

        verify(propertyRepository).findById(updateRequest.getId());
        verify(propertyRepository, never()).save(any(PropertyEntity.class));

    }

    @Test
    void deleteProperty_ShouldVerifyIfDeleteById_WhenDeletingAProperty() {


        propertyUseCase.deleteProperty(1L);

        verify(propertyRepository).deleteById(1L);
    }
}