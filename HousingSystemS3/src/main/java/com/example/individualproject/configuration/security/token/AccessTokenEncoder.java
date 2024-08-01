package com.example.individualproject.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
