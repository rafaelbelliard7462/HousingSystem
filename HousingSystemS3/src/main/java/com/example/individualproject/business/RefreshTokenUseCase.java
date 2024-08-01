package com.example.individualproject.business;

import com.example.individualproject.domain.RefreshTokenRequest;
import com.example.individualproject.domain.RenewAccessTokenResponse;

public interface RefreshTokenUseCase {
    String createRefreshToken(String email);
    RenewAccessTokenResponse renewAccessToken(RefreshTokenRequest request);
}
