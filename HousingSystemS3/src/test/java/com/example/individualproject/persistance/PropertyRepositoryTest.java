package com.example.individualproject.persistance;

import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.entity.AddressEntity;
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
class PropertyRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    void save_shouldSavePropertyWithAllFields(){
        UserEntity userEntity = createTestUser();
        AddressEntity addressEntity = createTestAddress();

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .user(userEntity)
                .propertyType(PropertyType.ROOM)
                .address(addressEntity)
                .price(450.00)
                .description("Some description")
                .available( new Date(2023,10,03))
                .room(1)
                .build();



        PropertyEntity savedProperty = propertyRepository.save(propertyEntity);
        assertNotNull(savedProperty.getId());

        savedProperty = entityManager.find(PropertyEntity.class, savedProperty.getId());

        PropertyEntity expectedUser = PropertyEntity.builder()
                .id(savedProperty.getId())
                .user(userEntity)
                .propertyType(PropertyType.ROOM)
                .address(addressEntity)
                .price(450.00)
                .description("Some description")
                .available( new Date(2023,10,03))
                .room(1)
                .build();

        assertEquals(expectedUser,savedProperty);
    }
    @Test
    void existsByStreet_shouldReturnTrue(){
        PropertyEntity propertyEntity = createTestProperty();


        assertTrue(propertyRepository.existsByStreet(propertyEntity.getAddress().getStreet()));
    }
    @Test
    void existsByStreet_shouldReturnFalse(){


        assertFalse(propertyRepository.existsByStreet("12 RandomStreet"));
    }

    @Test
    void findById_shouldBeEqualWithAllFields(){
        Optional<PropertyEntity> expectedResult = Optional.of(createTestProperty());

        Optional<PropertyEntity>  actualResult = propertyRepository.findById(expectedResult.get().getId());
        assertEquals(expectedResult,actualResult);
    }
    @Test
    void findById_shouldReturnEmptyOptional(){

        Optional<PropertyEntity>  actualResult = propertyRepository.findById(1L);
        assertEquals(Optional.empty(),actualResult);
    }

    @Test
    void findAllByUser_shouldBeTrue(){
        PropertyEntity expectedResult = createTestProperty();

        List<PropertyEntity>  actualResult = propertyRepository.findAllByUserId(expectedResult.getUser().getId());
        assertTrue(actualResult.contains(expectedResult));
    }


    @Test
    void findAll_shouldBeTrue() {

        PropertyEntity propertyEntity = createTestProperty();

        List<PropertyEntity> actualResult = propertyRepository.findAll();

        assertTrue(actualResult.contains(propertyEntity));
    }

    @Test
    void deleteById_shouldReturnEmptyOptionalAfterBeingDeleted(){
        PropertyEntity propertyEntity = createTestProperty();

        propertyRepository.deleteById(propertyEntity.getId());
        Optional<PropertyEntity>  actualResult = propertyRepository.findById(propertyEntity.getId());

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

    public AddressEntity createTestAddress(){
        return  entityManager.merge(AddressEntity.builder()
                .street("Molen 741")
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
                .available( new Date())
                .room(1)
                .build());
    }



}