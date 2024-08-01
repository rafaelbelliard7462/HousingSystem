package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class User {
    private  Long id;
    private String firstName;
    private  String lastName;
    private Date dateOfBirth;
    private  String email;
    private  String password;
    private Role role;
    @Builder.Default
    private List<Property> properties = new ArrayList<>();
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

}
