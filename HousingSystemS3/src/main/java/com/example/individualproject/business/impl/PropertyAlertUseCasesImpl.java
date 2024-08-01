package com.example.individualproject.business.impl;

import com.example.individualproject.business.EmailSenderUseCase;
import com.example.individualproject.business.PropertyAlertUseCases;
import com.example.individualproject.persistance.PreferenceRepository;
import com.example.individualproject.persistance.entity.PreferenceEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PropertyAlertUseCasesImpl implements PropertyAlertUseCases {
    private final PreferenceRepository preferenceRepository;
    private final EmailSenderUseCase emailSenderUseCase;
    @Override
    public void sendPropertyAlertToUser(PropertyEntity property) {

        List<PreferenceEntity> preferenceEntities= preferenceRepository.findByCityAndAndPropertyTypeAndPriceAndRoom(property.getAddress().getCity(),
                                                                                                                    property.getPropertyType(),
                                                                                                                    property.getPrice(), property.getRoom());
        List<UserEntity> userEntities = new ArrayList<>();
        for (PreferenceEntity p: preferenceEntities
             ) {
            userEntities.add(p.getUser());
        }

        for (UserEntity u: userEntities
        ) {sendClientEmail(property, u);
        }
    }
    private void sendClientEmail(PropertyEntity property, UserEntity user){
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


        String fullName = user.getFirstName() + " " + user.getLastName();
        String address = property.getAddress().getStreet() + ", " +property.getAddress().getCity() + ", "+ property.getAddress().getPostCode();

        String emailBody = String.format(htmlTemplate, fullName, address, property.getPrice(), property.getRoom(), property.getAvailable().toString(), property.getDescription(), property.getId());

        emailSenderUseCase.sendEmail(user.getEmail(), subject, emailBody);
    }
}
