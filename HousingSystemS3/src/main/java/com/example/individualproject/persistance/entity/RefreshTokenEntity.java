package com.example.individualproject.persistance.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "refresh_token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private  Long id;

    @NotBlank
    @Column(name = "token")
    private String token;
    @NotNull
    @Column(name = "expiry_date")
    private Date expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
