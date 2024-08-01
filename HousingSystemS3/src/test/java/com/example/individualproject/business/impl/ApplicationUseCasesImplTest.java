package com.example.individualproject.business.impl;

import com.example.individualproject.business.exception.AlreadyExistException;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.ApplicationRepository;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCasesImplTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private ApplicationUseCasesImpl applicationUseCases;


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
    void createApplication_shouldBeEqual_WhenCreatingAnApplication() {
        CreateApplicationRequest request = CreateApplicationObject();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationRepository.save(any(ApplicationEntity.class)))
                .thenReturn(applicationEntity);
        when(userRepository.findById(request.getUser().getId()))
                .thenReturn(Optional.of(getUserEntity(request.getUser())));
        when(propertyRepository.findById(request.getProperty().getId()))
                .thenReturn(Optional.of(getPropertyEntity(request.getProperty())));

        CreateApplicationResponse actualResult = applicationUseCases.createApplication(request);

        Application application = Application.builder()
                .id(1L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        CreateApplicationResponse expectedResult = CreateApplicationResponse.builder().applicationId(application.getId()).build();

        assertEquals(expectedResult, actualResult);
        verify(applicationRepository).save(any(ApplicationEntity.class));
        verify(userRepository).findById(request.getUser().getId());
        verify(propertyRepository).findById(request.getProperty().getId());
    }
    @Test
    void createApplication_shouldThrowException_WhenCreatingAnApplication() {
        CreateApplicationRequest request = CreateApplicationObject();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationRepository.existsByUser_IdAndProperty_Id(applicationEntity.getUser().getId(),applicationEntity.getProperty().getId()))
                .thenReturn(true);



        assertThrows(AlreadyExistException.class, () -> applicationUseCases.createApplication(request));
        verify(applicationRepository).existsByUser_IdAndProperty_Id(applicationEntity.getUser().getId(),applicationEntity.getProperty().getId());
    }

    @Test
    void getApplicationsByUser_shouldBeEqual_WhenGettingApplicationByUserId() {
        CreateApplicationRequest request = CreateApplicationObject();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationRepository.findAllByUserId(1L))
                .thenReturn(List.of(applicationEntity));

        GetApplicationsResponse actualResult = applicationUseCases.getApplicationsByUser(1L);


        Application application = Application.builder()
                .id(1L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        GetApplicationsResponse expectedResult = GetApplicationsResponse.builder()
                                                .applications(List.of(application))
                                                .build();

        assertEquals(expectedResult, actualResult);
        verify(applicationRepository).findAllByUserId(1L);

    }

    @Test
    void getApplicationsByUser_shouldBeEqual_WhenGettingApplicationsListIsEmpty() {


        when(applicationRepository.findAllByUserId(1L))
                .thenReturn(List.of());

        GetApplicationsResponse actualResult = applicationUseCases.getApplicationsByUser(1L);


        GetApplicationsResponse expectedResult = GetApplicationsResponse.builder()
                .applications(List.of())
                .build();

        assertEquals(expectedResult, actualResult);
        verify(applicationRepository).findAllByUserId(1L);

    }


    @Test
    void getApplicationsByProperty_shouldBeEqual_WhenGettingApplicationByPropertyId() {
        CreateApplicationRequest request = CreateApplicationObject();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationRepository.findAllByPropertyId(1L))
                .thenReturn(List.of(applicationEntity));

        GetApplicationsResponse actualResult = applicationUseCases.getApplicationsByProperty(1L);


        Application application = Application.builder()
                .id(1L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        GetApplicationsResponse expectedResult = GetApplicationsResponse.builder()
                .applications(List.of(application))
                .build();

        assertEquals(expectedResult, actualResult);
        verify(applicationRepository).findAllByPropertyId(1L);
    }

    @Test
    void getApplicationsByProperty_shouldBeEqual_WhenGettingApplicationsListIsEmpty() {


        when(applicationRepository.findAllByPropertyId(1L))
                .thenReturn(List.of());

        GetApplicationsResponse actualResult = applicationUseCases.getApplicationsByProperty(1L);


        GetApplicationsResponse expectedResult = GetApplicationsResponse.builder()
                .applications(List.of())
                .build();

        assertEquals(expectedResult, actualResult);
        verify(applicationRepository).findAllByPropertyId(1L);

    }

    @Test
    void getApplicationById_shouldBeEqual_whenGettingApplicationById() {
        CreateApplicationRequest request = CreateApplicationObject();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(getPropertyEntity(request.getProperty()))
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(applicationEntity));

        Optional<Application> optionalApplication = applicationUseCases.getApplicationById(1L);
        Application actualResult = optionalApplication.orElseThrow();

        Application expectedResult = Application.builder()
                .id(1L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        assertEquals(expectedResult, actualResult);
        verify(applicationRepository).findById(1L);

    }

    @Test
    void getApplicationById_shouldThrowAnException_whenGettingApplicationById() {


        when(applicationRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> applicationUseCases.getApplicationById(1L));
        verify(applicationRepository).findById(1L);

    }

    @Test
    void updateApplication_shouldBeEqual_whenUpdatingApplication() {
        CreateApplicationRequest request = CreateApplicationObject();
        UpdateApplicationRequest updateRequest = UpdateApplicationRequest.builder()
                .id(1L)
                .appliedDate(request.getAppliedDate())
                .status(Status.ACCEPTED)
                .description(request.getDescription())
                .build();
    PropertyEntity propertyEntity = getPropertyEntity(request.getProperty());
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .property(propertyEntity)
                .appliedDate(updateRequest.getAppliedDate())
                .status(updateRequest.getStatus())
                .description(updateRequest.getDescription())
                .build();
        propertyEntity.getApplications().add(applicationEntity);

        when(applicationRepository.findById(updateRequest.getId()))
                .thenReturn(Optional.of(applicationEntity));

        when(propertyRepository.findById(applicationEntity.getProperty().getId()))
                .thenReturn(Optional.of(propertyEntity));
        applicationUseCases.updateApplication(updateRequest);

        verify(applicationRepository).findById(updateRequest.getId());
        verify(applicationRepository, times(2)).save(applicationEntity);

    }

    @Test
    void updateApplication_shouldThrowAnException_whenUpdatingApplication() {
        CreateApplicationRequest request = CreateApplicationObject();
        UpdateApplicationRequest updateRequest = UpdateApplicationRequest.builder()
                .id(1L)
                .appliedDate(request.getAppliedDate())
                .status(Status.ACCEPTED)
                .description(request.getDescription())
                .build();


        when(applicationRepository.findById(updateRequest.getId()))
                .thenReturn(Optional.empty());



        assertThrows(NotFoundException.class, ()-> applicationUseCases.updateApplication(updateRequest) );
        verify(applicationRepository).findById(updateRequest.getId());
        verify(applicationRepository, never()).save(any(ApplicationEntity.class));

    }
    @Test
    void deleteApplication_ShouldVerifyIfDeleteById_whenDeletingApplicationById() {

        applicationUseCases.deleteApplication(1L);

        verify(applicationRepository).deleteById(1L);
    }


}