package com.example.individualproject.domain;

import lombok.*;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class RefreshToken {
    private  Long id;
    private String token;
    private Date expiryDate;
    private  User user;
}
