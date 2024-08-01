package com.example.individualproject.business.impl;

import com.example.individualproject.business.exception.UnauthorizedDataAccessException;
import com.example.individualproject.configuration.security.token.AccessToken;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.ApplicationRepository;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import com.example.individualproject.persistance.specification.ApplicationSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAndFilterUseCasesImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private  SearchAndFilterUseCasesImpl searchAndFilterUseCases;
    CreateApplicationRequest CreateApplicationObject(){

        User user = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .applications(new ArrayList<>())
                .properties(new ArrayList<>())
                .build();

        Address address = Address.builder()
                .id(1L)
                .street("Molenslag 62")
                .city("Breda")
                .postCode("4817GZ")
                .build();

        Property property = Property.builder()
                .id(1L)
                .user(user)
                .propertyType(PropertyType.ROOM)
                .address(address)
                .price(450.00)
                .description("Some description")
                .available( new Date())
                .room(1)
                .applications(new ArrayList<>())
                .build();


        return CreateApplicationRequest.builder()
                .user(user)
                .property(property)
                .appliedDate(new Date())
                .status(Status.PENDING)
                .description("Some description")
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
                .applications(new ArrayList<>())
                .build();
    }

    public PropertyEntity getPropertyEntity(Property property){
        return  PropertyEntity.builder()
                .id(property.getId())
                .user(getUserEntity(property.getUser()))
                .price(property.getPrice())
                .address(getAddressEntity(property.getAddress()))
                .room(property.getRoom())
                .available(property.getAvailable())
                .propertyType(property.getPropertyType())
                .description(property.getDescription())
                .applications(new ArrayList<>())
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
    void getApplicationsByUser_shouldReturnApplications_whenUserHasApplications() {
        CreateApplicationRequest request = CreateApplicationObject();
        // Arrange
        Long userId = 1L;
        Status status = Status.PENDING;
        Pageable pageable = PageRequest.of(0, 10);
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        Page<ApplicationEntity> applicationsPage = new PageImpl<>(Collections.singletonList(applicationEntity), pageable, 1);
        when(applicationRepository.findByProperty_User_IdAndStatus(userId, status, pageable)).thenReturn(applicationsPage);

        // Act
        Page<Application> result = searchAndFilterUseCases.getApplicationsByUser(userId, status, pageable);

        Application application = Application.builder()
                .id(1L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getNumberOfElements());
        assertEquals(application, result.getContent().get(0));

        verify(applicationRepository).findByProperty_User_IdAndStatus(userId, status, pageable);
    }

    @Test
    void searchApplicationsFromUser_shouldReturnApplications_whenUserHasApplications() {
        CreateApplicationRequest request = CreateApplicationObject();
        // Arrange
        Long userId = 1L;
        Status status = Status.PENDING;
        String searchString = "Molenslag";
        Pageable pageable = PageRequest.of(0, 10);
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        Page<ApplicationEntity> applicationsPage = new PageImpl<>(Collections.singletonList(applicationEntity), pageable, 1);
        when(applicationRepository.findAll(any(ApplicationSpecification.class), eq(pageable))).thenReturn(applicationsPage);

        // Act
        Page<Application> result = searchAndFilterUseCases.searchApplicationsFromUser(userId, status, searchString, pageable);

        Application application = Application.builder()
                .id(1L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getNumberOfElements());
        assertEquals(application, result.getContent().get(0));

        verify(applicationRepository).findAll(any(ApplicationSpecification.class), eq(pageable));
    }

    @Test
    void searchProperties_shouldBeEqual_WhenCriteriaProvided() {

        String city = "Breda";
        double maxPrice = 0.0;
        int minSize = 50;
        PropertyType propertyType = PropertyType.APARTMENT;
        boolean rented = false;
        Pageable pageable = Pageable.unpaged();
        List<PropertyEntity> propertyEntities = new ArrayList<>(); // Populate with mock data
        when(propertyRepository.findByCityAndPriceAndSize(city, Double.MAX_VALUE, minSize, propertyType,rented, pageable))
                .thenReturn(new PageImpl<>(propertyEntities));


        Page<Property> resultPage = searchAndFilterUseCases.searchProperties(city, maxPrice, minSize, propertyType, rented, pageable);


        verify(propertyRepository).findByCityAndPriceAndSize(city, Double.MAX_VALUE, minSize, propertyType, rented, pageable);

        assertEquals(propertyEntities.size(), resultPage.getContent().size());
    }

    @Test
    void searchProperties_shouldBeEqual_WhenNoCriteriaProvided() {

        Pageable pageable = Pageable.unpaged();

        List<PropertyEntity> propertyEntities = new ArrayList<>(); // Populate with mock data
        when(propertyRepository.findAllByRented(eq(false), any(Pageable.class)))
                .thenReturn(new PageImpl<>(propertyEntities));


        Page<Property> resultPage = searchAndFilterUseCases.searchProperties(null, 0.0, 0, null, false, pageable);

        verify(propertyRepository).findAllByRented(false, pageable);
        assertEquals(propertyEntities.size(), resultPage.getContent().size());
    }
    @Test
    void searchHomeownerProperties_shouldReturnProperties_whenValidUserId() {
        // Arrange
        Long userId = 1L;
        String city = "Breda";
        double maxPrice = 0.0;
        int minSize = 50;
        PropertyType propertyType = PropertyType.APARTMENT;
        boolean rented = false;
        Pageable pageable = Pageable.unpaged();

        List<PropertyEntity> propertyEntities = new ArrayList<>(); // Populate with mock data
        when(propertyRepository.findByCityAndPriceAndSizeAndUserId(city, Double.MAX_VALUE, minSize, propertyType,rented,userId ,pageable))
                .thenReturn(new PageImpl<>(propertyEntities));

        // Mocking the RequestAccessToken to return the valid user id
        when(requestAccessToken.getUserId()).thenReturn(userId);


        // Act
        Page<Property> result = searchAndFilterUseCases.searchHomeownerProperties(city, maxPrice, minSize, propertyType, rented, userId, pageable);

        assertEquals(propertyEntities.size(), result.getContent().size());
        // Assert
        verify(propertyRepository).findByCityAndPriceAndSizeAndUserId(city, Double.MAX_VALUE, minSize, propertyType, rented, userId,pageable);
    }

    @Test
    void searchHomeownerProperties_shouldThrowUnauthorizedAccessException_whenInvalidUserId() {
        // Arrange
        Long userId = 2L;  // Different from the user id in the RequestAccessToken
        String city = "Breda";
        double maxPrice = 500.0;
        int minSize = 50;
        PropertyType propertyType = PropertyType.APARTMENT;
        boolean rented = false;
        Pageable pageable = Pageable.unpaged();

        // Mocking the RequestAccessToken to return a different user id
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // Act and Assert
        assertThrows(UnauthorizedDataAccessException.class, () ->
                searchAndFilterUseCases.searchHomeownerProperties(city, maxPrice, minSize, propertyType, rented, userId, pageable));

        verify(requestAccessToken).getUserId();
        verifyNoInteractions(propertyRepository);
    }
    @Test
    void searchHomeownerProperties_shouldBeEqual_WhenNoCriteriaProvided() {

        Pageable pageable = Pageable.unpaged();

        List<PropertyEntity> propertyEntities = new ArrayList<>(); // Populate with mock data
        when(propertyRepository.findAllByRentedAndUser_Id(eq(false), eq(1L),any(Pageable.class)))
                .thenReturn(new PageImpl<>(propertyEntities));

        when(requestAccessToken.getUserId()).thenReturn(1L);

        Page<Property> resultPage = searchAndFilterUseCases.searchHomeownerProperties(null, 0.0, 0, null, false, 1L,pageable);

        verify(propertyRepository).findAllByRentedAndUser_Id(false, 1L, pageable);
        assertEquals(propertyEntities.size(), resultPage.getContent().size());
    }

}