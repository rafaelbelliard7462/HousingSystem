package com.example.individualproject.business.impl;

import com.example.individualproject.business.RefreshTokenUseCase;
import com.example.individualproject.business.exception.InvalidCredentialsException;
import com.example.individualproject.configuration.security.token.AccessTokenEncoder;
import com.example.individualproject.configuration.security.token.impl.AccessTokenImpl;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  AccessTokenEncoder accessTokenEncoder;
    @Mock
    private   RefreshTokenUseCase refreshTokenUseCase;
    @InjectMocks
    private  LoginUseCaseImpl loginUseCase;
    @Test
    void login_shouldBeEqual_WhenLoggingIn() {

        LoginRequest request = LoginRequest.builder().email("bobsmith@gmil.com").password("Bobby!").build();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();


        when(userRepository.findByEmail("bobsmith@gmil.com"))
                .thenReturn(Optional.of(userEntity));

        when(passwordEncoder.matches(userEntity.getPassword(), request.getPassword()))
                .thenReturn(true);

        when(accessTokenEncoder.encode(new AccessTokenImpl(userEntity.getEmail(), userEntity.getId(), Set.of(userEntity.getRole().toString()) )))
                .thenReturn("123456");

        when(refreshTokenUseCase.createRefreshToken(userEntity.getEmail()))
                .thenReturn("qwerty");

        LoginResponse actualResult = loginUseCase.login(request);


        LoginResponse expectedResult = LoginResponse.builder()
                .accessToken("123456")
                .refreshToken("qwerty")
                .build();
        assertEquals(expectedResult,actualResult);

        verify(userRepository).findByEmail("bobsmith@gmil.com");
        verify(passwordEncoder).matches(userEntity.getPassword(), request.getPassword());
        verify(accessTokenEncoder).encode(new AccessTokenImpl(userEntity.getEmail(), userEntity.getId(), Set.of(userEntity.getRole().toString())));
        verify(refreshTokenUseCase).createRefreshToken(userEntity.getEmail());

    }
    @Test
    void login_shouldThrowInvalidCredentialsException_WhenUserNotFound() {

        LoginRequest request = LoginRequest.builder().email("bobsmith@gmil.com").password("Bobby!").build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());



        assertThrows(InvalidCredentialsException.class, () ->loginUseCase.login(request) );

        verify(userRepository).findByEmail("bobsmith@gmil.com");
        verify(passwordEncoder, never()).matches(userEntity.getPassword(), request.getPassword());
        verify(accessTokenEncoder, never()).encode(new AccessTokenImpl(userEntity.getEmail(), userEntity.getId(), Set.of(userEntity.getRole().toString())));
        verify(refreshTokenUseCase, never()).createRefreshToken(userEntity.getEmail());

    }
    @Test
    void login_shouldThrowInvalidCredentialsException_WhenPasswordDoesNotMatch() {

        LoginRequest request = LoginRequest.builder().email("bobsmith@gmil.com").password("Bobby!").build();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();


        when(userRepository.findByEmail("bobsmith@gmil.com"))
                .thenReturn(Optional.of(userEntity));

        when(passwordEncoder.matches(userEntity.getPassword(), request.getPassword()))
                .thenReturn(false);



        assertThrows(InvalidCredentialsException.class, () ->loginUseCase.login(request) );

        verify(userRepository).findByEmail("bobsmith@gmil.com");
        verify(passwordEncoder).matches(userEntity.getPassword(), request.getPassword());
        verify(accessTokenEncoder, never()).encode(new AccessTokenImpl(userEntity.getEmail(), userEntity.getId(), Set.of(userEntity.getRole().toString())));
        verify(refreshTokenUseCase, never()).createRefreshToken(userEntity.getEmail());

    }

}