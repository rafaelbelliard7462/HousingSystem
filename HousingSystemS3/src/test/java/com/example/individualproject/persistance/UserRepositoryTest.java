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
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private EntityManager entityManager; // Inject EntityManager

    @Test
     void testAddPropertyToUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(new Date());
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setRole(Role.HOME_SEEKER);
        entityManager.persist(user);

        AddressEntity address = new AddressEntity();
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setPostCode("62701");
        entityManager.persist(address);

        PropertyEntity property = new PropertyEntity();
        property.setUser(user);
        property.setAddress(address);
        property.setPropertyType(PropertyType.HOUSE);
        property.setPrice(100000.0);
        property.setRoom(3);
        property.setAvailable(new Date());
        property.setDescription("A beautiful house");
        property.setRented(false);
        entityManager.persist(property);
        user.getProperties().add(property);

        // Act: retrieve the user
        UserEntity foundUser = userRepository.findById(user.getId()).get();

        // Assert: check that properties are retrieved correctly
        assertEquals(List.of(property), foundUser.getProperties());
    }



    private AddressEntity createAddress() {
        // Create and return a sample address for testing
        // You can adjust this method based on your needs
        return AddressEntity.builder()
                .street("123 Main Street")
                .city("Sample City")
                .postCode("12345")
                .build();
    }
    @Test
    void save_shouldSaveUserWithAllFields(){
        UserEntity userEntity = UserEntity.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsy7mith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .applications(List.of())
                .properties(List.of())
                .build();


        UserEntity savedUser = userRepository.save(userEntity);
        assertNotNull(savedUser.getId());

        savedUser = entityManager.find(UserEntity.class, savedUser.getId());

        UserEntity expectedUser = UserEntity.builder()
                .id(savedUser.getId())
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsy7mith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .build();

    assertEquals(expectedUser,savedUser);
    }

    @Test
    void existByEmail_shouldBeTrue(){
        UserEntity userEntity = createTestUser();


     assertTrue(userRepository.existsByEmail(userEntity.getEmail()));
    }
    @Test
    void existByEmail_shouldBeFalse(){

        assertFalse(userRepository.existsByEmail("random@gmail.com"));
    }

  @Test
    void findById_shouldBeEqualWithAllFields(){
        Optional<UserEntity> expectedResult = Optional.of(createTestUser());

        Optional<UserEntity>  actualResult = userRepository.findById(expectedResult.get().getId());
        assertEquals(expectedResult,actualResult);
    }
    @Test
    void findById_shouldReturnEmptyOptional(){

        Optional<UserEntity>  actualResult = userRepository.findById(1L);
        assertEquals(Optional.empty(),actualResult);
    }

    @Test
    void findByEmail_shouldBeEqualWithAllFields(){
        Optional<UserEntity> expectedResult = Optional.of(createTestUser());

        Optional<UserEntity>  actualResult = userRepository.findByEmail(expectedResult.get().getEmail());
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void findByEmail_shouldReturnEmptyOptional(){

        Optional<UserEntity>  actualResult = userRepository.findByEmail("random@gmail.com");
        assertEquals(Optional.empty(),actualResult);
    }
    @Test
    void findAll_shouldBeTrue() {

        UserEntity userEntity = createTestUser();

        List<UserEntity> actualResult = userRepository.findAll();

        assertTrue(actualResult.contains(userEntity));
    }

    @Test
    void deleteById_shouldBeNull(){
        UserEntity userEntity = createTestUser();

        userRepository.deleteById(userEntity.getId());
        Optional<UserEntity>  actualResult = userRepository.findByEmail("random@gmail.com");

        assertEquals(Optional.empty(),actualResult);

    }


    private UserEntity createTestUser(){
        return entityManager.merge(UserEntity.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith4723@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(2000,10,03))
                .role(Role.HOMEOWNER)
                .applications(List.of())
                .properties(List.of())
                .build());
    }
}