package com.example.individualproject.persistance.entity;

import com.example.individualproject.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private  Long id;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "firstName")
    private String firstName;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "lastName")
    private  String lastName;

    @NotNull
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "email")
    private  String email;

    @NotBlank
    @Length( max = 100)
    @Column(name = "password")
    private  String password;

    @NotNull
    @Column(name = "role")
    private Role role;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<PropertyEntity> properties = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<ApplicationEntity> applications = new ArrayList<>();
}
