package com.example.individualproject.business;

import com.example.individualproject.domain.LoginRequest;
import com.example.individualproject.domain.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
