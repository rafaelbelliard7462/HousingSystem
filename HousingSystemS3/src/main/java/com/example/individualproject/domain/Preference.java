package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.PropertyType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Preference {
    private  Long id;
    private User user;
    private String city;
    private PropertyType propertyType;
    private  double price;
    private  int room;
}
