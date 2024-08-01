package com.example.individualproject.persistance;

import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApplicationRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private  ApplicationRepository applicationRepository;
    @Test
    void save_shouldSaveUserWithAllFields(){
        UserEntity userEntity = createTestUser();
        PropertyEntity propertyEntity = createTestProperty();

        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .user(userEntity)
                .property(propertyEntity)
                .appliedDate(new Date(2023,10,03))
                .status(Status.PENDING)
                .description("Some description")
                .build();

        ApplicationEntity savedApplication = applicationRepository.save(applicationEntity);
        assertNotNull(savedApplication.getId());

        savedApplication = entityManager.find(ApplicationEntity.class, savedApplication.getId());

        ApplicationEntity expectedApplication = ApplicationEntity.builder()
                .id(savedApplication.getId())
                .user(userEntity)
                .property(propertyEntity)
                .appliedDate(new Date(2023,10,03))
                .status(Status.PENDING)
                .description("Some description")
                .build();

        assertEquals(expectedApplication,savedApplication);
    }
    @Test
    void existsByUserAndProperty_shouldReturnTrue(){
      ApplicationEntity applicationEntity = createTestApplication();


        assertTrue(applicationRepository.existsByUser_IdAndProperty_Id(applicationEntity.getUser().getId(),applicationEntity.getProperty().getId()));
    }
    @Test
    void existsByUserAndProperty_shouldReturnFalse(){


        assertFalse(applicationRepository.existsByUser_IdAndProperty_Id(createTestUser().getId(),createTestProperty().getId()));
    }

    @Test
    void findById_shouldBeEqualWithAllFields(){
        Optional<ApplicationEntity> expectedResult = Optional.of(createTestApplication());

        Optional<ApplicationEntity>  actualResult = applicationRepository.findById(expectedResult.get().getId());
        assertEquals(expectedResult,actualResult);
    }
    @Test
    void findById_shouldReturnEmptyOptional(){

        Optional<ApplicationEntity>  actualResult = applicationRepository.findById(1L);
        assertEquals(Optional.empty(),actualResult);
    }

    @Test
    void findAllByUserId_shouldBeTrue(){
        ApplicationEntity expectedResult = createTestApplication();

        List<ApplicationEntity> actualResult = applicationRepository.findAllByUserId(expectedResult.getUser().getId());
        assertTrue(actualResult.contains(expectedResult));
    }

    @Test
    void findAllByPropertyId_shouldBeTrue(){
        ApplicationEntity expectedResult = createTestApplication();

        List<ApplicationEntity> actualResult = applicationRepository.findAllByPropertyId(expectedResult.getProperty().getId());
        assertTrue(actualResult.contains(expectedResult));
    }


    @Test
    void deleteById_shouldReturnEmptyOptionalAfterBeingDeleted(){
        ApplicationEntity applicationEntity = createTestApplication();

        applicationRepository.deleteById(applicationEntity.getId());
        Optional<ApplicationEntity>  actualResult = applicationRepository.findById(applicationEntity.getId());

        assertEquals(Optional.empty(),actualResult);

    }


    private UserEntity createTestUser(){
        return entityManager.merge(UserEntity.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith44@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .build());
    }

    public AddressEntity createTestAddress(){
        return  entityManager.merge(AddressEntity.builder()
                .street("Molenslag 33")
                .city("Breda")
                .postCode("4817GZ")
                .build());
    }

    private  PropertyEntity createTestProperty(){

        return entityManager.merge(PropertyEntity.builder()
                .user(createTestUser())
                .propertyType(PropertyType.ROOM)
                .address(createTestAddress())
                .price(450.00)
                .description("Some description")
                .available( new Date(2023, 11, 13))
                .room(1)
                .build());
    }
    private ApplicationEntity createTestApplication(){
        return entityManager.merge(ApplicationEntity.builder()
                .user(createTestUser())
                .property(createTestProperty())
                .appliedDate(new Date())
                .status(Status.PENDING)
                .description("Some description")
                .build());
    }

}