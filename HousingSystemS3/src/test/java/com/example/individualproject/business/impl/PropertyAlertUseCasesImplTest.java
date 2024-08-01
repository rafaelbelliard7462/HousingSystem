package com.example.individualproject.business.impl;

import com.example.individualproject.business.EmailSenderUseCase;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.PreferenceRepository;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.PreferenceEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyAlertUseCasesImplTest {

    @Mock
    private  PreferenceRepository preferenceRepository;
    @Mock
    private  EmailSenderUseCase emailSenderUseCase;
    @InjectMocks
    private PropertyAlertUseCasesImpl propertyAlertUseCases;
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
                .id(2L)
                .user(user)
                .propertyType(PropertyType.ROOM)
                .address(address)
                .price(450.00)
                .description("Some description")
                .available( new Date())
                .room(1)
                .build();


    }
    PreferenceEntity CreatePreferenceEntity() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobvsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOME_SEEKER)
                .properties(new ArrayList<>())
                .applications(new ArrayList<>())
                .build();


        return PreferenceEntity.builder()
                .id(2L)
                .user(user)
                .propertyType(PropertyType.ROOM)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build();
    }
    @Test
    void sendPropertyAlertToUser_shouldBeVerified_whenSendingPropertyAlert() {
        PropertyEntity propertyEntity =CreatePropertyEntity();
        PreferenceEntity preferenceEntity = CreatePreferenceEntity();




        when(preferenceRepository.findByCityAndAndPropertyTypeAndPriceAndRoom(propertyEntity.getAddress().getCity(),
                propertyEntity.getPropertyType(),
                propertyEntity.getPrice(), propertyEntity.getRoom()))
                .thenReturn(List.of(preferenceEntity));

        propertyAlertUseCases.sendPropertyAlertToUser(propertyEntity);

        String subject = "Property Alert!";

        String htmlTemplate = """
        <html>
            <body>
                <h1>Property alert</h1>
                <p>Dear %s, there's a property that matches your preference.</p>
                        
                <h3>Property Details</h3>
                
                <p>Address: %s</p>
                <p>Price: %s</p>
                <p>Nr. of room: %s</p>
                <p>Available: %s</p>
                <p>Description: %s</p>

                <!-- Adding a hyperlink with dynamic property ID -->
                <p>View Property Information: <a href="http://127.0.0.1:5173/propertyInfo/%s">Click here to view</a></p>
            </body>
        </html>
        """;

        String fullName = preferenceEntity.getUser().getFirstName() + " " + preferenceEntity.getUser().getLastName();
        String address = propertyEntity.getAddress().getStreet() + ", " +propertyEntity.getAddress().getCity() + ", "+ propertyEntity.getAddress().getPostCode();

        String emailBody = String.format(htmlTemplate, fullName, address, propertyEntity.getPrice(), propertyEntity.getRoom(), propertyEntity.getAvailable().toString(), propertyEntity.getDescription(), propertyEntity.getId());


        verify(preferenceRepository).findByCityAndAndPropertyTypeAndPriceAndRoom(propertyEntity.getAddress().getCity(),
                propertyEntity.getPropertyType(),
                propertyEntity.getPrice(), propertyEntity.getRoom());
        verify(emailSenderUseCase).sendEmail(preferenceEntity.getUser().getEmail(), subject, emailBody);

    }

}