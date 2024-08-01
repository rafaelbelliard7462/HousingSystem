package com.example.individualproject.controller;

import com.example.individualproject.business.LoginUseCase;
import com.example.individualproject.business.RefreshTokenUseCase;
import com.example.individualproject.domain.LoginRequest;
import com.example.individualproject.domain.LoginResponse;
import com.example.individualproject.domain.RefreshTokenRequest;
import com.example.individualproject.domain.RenewAccessTokenResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@AllArgsConstructor
public class LoginController {
    private final LoginUseCase loginUseCase;
    private  final RefreshTokenUseCase renewAccessTokenUseCase;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
    //@RolesAllowed({"HomeSeeker","Homeowner"})
    @PostMapping("/renew")
    public  ResponseEntity<RenewAccessTokenResponse> renewAccessToken(@RequestBody @Valid RefreshTokenRequest request){
        RenewAccessTokenResponse response = renewAccessTokenUseCase.renewAccessToken(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
