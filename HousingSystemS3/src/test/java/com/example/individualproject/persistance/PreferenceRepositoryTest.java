package com.example.individualproject.persistance;

import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.entity.PreferenceEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PreferenceRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Test
    void save_shouldSavePreferenceWithAllFields(){
        UserEntity userEntity = createTestUser();

        PreferenceEntity preferenceEntity = PreferenceEntity.builder()
                .user(userEntity)
                .propertyType(PropertyType.ROOM)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build();



        PreferenceEntity savedPreference = preferenceRepository.save(preferenceEntity);
        assertNotNull(savedPreference.getId());

        savedPreference = entityManager.find(PreferenceEntity.class, savedPreference.getId());

        PreferenceEntity expectedUser = PreferenceEntity.builder()
                .id(savedPreference.getId())
                .user(userEntity)
                .propertyType(PropertyType.ROOM)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build();

        assertEquals(expectedUser,savedPreference);
    }

    @Test
    void findByUserId_shouldBeEqualWithAllFields(){
        Optional<PreferenceEntity> expectedResult = Optional.of(createTestPreference());

        Optional<PreferenceEntity>  actualResult = preferenceRepository.findByUserId(expectedResult.get().getUser().getId());
        assertEquals(expectedResult,actualResult);
    }
    @Test
    void findByUserId_shouldReturnEmptyOptional(){

        Optional<PreferenceEntity>  actualResult = preferenceRepository.findByUserId(1L);
        assertEquals(Optional.empty(),actualResult);
    }


    @Test
    void deleteById_shouldReturnEmptyOptionalAfterBeingDeleted(){
        PreferenceEntity preferenceEntity = createTestPreference();

        preferenceRepository.deleteById(preferenceEntity.getId());
        Optional<PreferenceEntity>  actualResult = preferenceRepository.findById(preferenceEntity.getId());

        assertEquals(Optional.empty(),actualResult);

    }


    private UserEntity createTestUser(){
        return entityManager.merge(UserEntity.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobbysmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .build());
    }



    private  PreferenceEntity createTestPreference(){

        return entityManager.merge(PreferenceEntity.builder()
                .user(createTestUser())
                .propertyType(PropertyType.ROOM)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build());
    }

}