package com.example.individualproject.business.impl;

import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.PreferenceRepository;
import com.example.individualproject.persistance.entity.PreferenceEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PreferenceUsesCasesImplTest {
    @Mock
    private PreferenceRepository preferenceRepository;
    @InjectMocks
    private PreferenceUsesCasesImpl preferenceUsesCases;

    CreatePreferenceRequest CreatePreferenceObject(){
        User user = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(new ArrayList<>())
                .applications(new ArrayList<>())
                .build();


        return CreatePreferenceRequest.builder()
                .user(user)
                .propertyType(PropertyType.ROOM)
                .city("Breda")
                .price(450.00)
                .room(1)
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
                .applications(new ArrayList<>())
                .build();
    }

    @Test
    void createPreference_shouldBeEqual_WhenAddingPreference() {

        CreatePreferenceRequest request = CreatePreferenceObject();
        PreferenceEntity preferenceEntity = PreferenceEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .propertyType(request.getPropertyType())
                .build();

        when(preferenceRepository.save(any(PreferenceEntity.class)))
                .thenReturn(preferenceEntity);
        CreatePreferenceResponse actualResult = preferenceUsesCases.createPreference(request);


        Preference preference = Preference.builder()
                .id(1L)
                .user(request.getUser())
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .propertyType(request.getPropertyType())
                .build();
        CreatePreferenceResponse expectedResult = CreatePreferenceResponse.builder().preferenceId(preference.getId()).build();
        assertEquals(expectedResult,actualResult);
        verify(preferenceRepository).save(any(PreferenceEntity.class));

    }


    @Test
    void getPreferenceByUserId_shouldBeEqual_WhenGettingPreferenceByUserId() {
        CreatePreferenceRequest request = CreatePreferenceObject();
        PreferenceEntity preferenceEntity = PreferenceEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .propertyType(request.getPropertyType())
                .build();
        when(preferenceRepository.findByUserId(preferenceEntity.getId()))
                .thenReturn(Optional.of(preferenceEntity));



        Optional<Preference> optionalPreference = preferenceUsesCases.getPreferenceByUserId(1L);
        Preference actualResult = optionalPreference.get();

        Preference expectedResult = Preference.builder()
                .id(1L)
                .user(request.getUser())
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .propertyType(request.getPropertyType())
                .build();

        assertEquals(expectedResult, actualResult);

    }
    @Test
    void getPreferenceByUserId_shouldThrowNotFountException_WhenGettingPreferenceByUserIdThatDontExist() {

        CreatePreferenceRequest request = CreatePreferenceObject();
        PreferenceEntity preferenceEntity = PreferenceEntity.builder()
                .id(1L)
                .user(getUserEntity(request.getUser()))
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .propertyType(request.getPropertyType())
                .build();
        when(preferenceRepository.findByUserId(preferenceEntity.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> preferenceUsesCases.getPreferenceByUserId(1L) );

        verify(preferenceRepository).findByUserId(preferenceEntity.getId());
    }
    @Test
    void updatePreference_shouldBeEqual_WhenUpdatingPreference() {
        CreatePreferenceRequest request = CreatePreferenceObject();


        UpdatePreferenceRequest updateRequest = UpdatePreferenceRequest.builder()
                .id(1L)
                .user(request.getUser())
                .propertyType(PropertyType.APARTMENT)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build();

        PreferenceEntity propertyEntity = PreferenceEntity.builder()
                .id(1L)
                .user(getUserEntity(updateRequest.getUser()))
                .propertyType(updateRequest.getPropertyType())
                .city(updateRequest.getCity())
                .price(updateRequest.getPrice())
                .room(updateRequest.getRoom())
                .build();

        when(preferenceRepository.findById(updateRequest.getId()))
                .thenReturn(Optional.of(propertyEntity));

        preferenceUsesCases.updatePreference(updateRequest);

        verify(preferenceRepository).findById(updateRequest.getId());
        verify(preferenceRepository).save(propertyEntity);

    }

    @Test
    void updatePreference_shouldThrowNotFoundException_WhenUpdatingPreferenceButIdDoesNotExist() {
        CreatePreferenceRequest request = CreatePreferenceObject();


        UpdatePreferenceRequest updateRequest = UpdatePreferenceRequest.builder()
                .id(1L)
                .user(request.getUser())
                .propertyType(PropertyType.APARTMENT)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build();
        when(preferenceRepository.findById(updateRequest.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> preferenceUsesCases.updatePreference(updateRequest));

        verify(preferenceRepository).findById(updateRequest.getId());
        verify(preferenceRepository, never()).save(any(PreferenceEntity.class));

    }

    @Test
    void deletePreference_ShouldVerifyIfDeleteById_WhenDeletingAPreference() {


        preferenceUsesCases.deletePreference(1L);

        verify(preferenceRepository).deleteById(1L);
    }

}