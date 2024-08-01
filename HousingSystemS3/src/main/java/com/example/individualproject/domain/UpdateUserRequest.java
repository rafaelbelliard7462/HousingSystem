package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UpdateUserRequest {

    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private  String lastName;
    @NotNull
    private Date dateOfBirth;
    @NotBlank
    private  String email;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
}
