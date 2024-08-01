package com.example.individualproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewAccessTokenResponse {
    private String accessToken;
    private  String refreshToken;
}
