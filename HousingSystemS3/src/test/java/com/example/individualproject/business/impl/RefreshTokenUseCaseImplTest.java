package com.example.individualproject.business.impl;

import com.example.individualproject.business.RefreshTokenUseCase;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.configuration.security.token.AccessToken;
import com.example.individualproject.configuration.security.token.AccessTokenEncoder;
import com.example.individualproject.configuration.security.token.impl.AccessTokenImpl;
import com.example.individualproject.domain.RefreshTokenRequest;
import com.example.individualproject.domain.RenewAccessTokenResponse;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.RefreshTokenRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.RefreshTokenEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseImplTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RefreshTokenRepository refreshTokenRepositoryMock;
    @Mock
    private AccessTokenEncoder accessTokenEncoder;
    @InjectMocks
    private RefreshTokenUseCaseImpl refreshTokenUseCases;
    @InjectMocks
    private UserUseCasesImpl userUseCases;
    @Test
     void createRefreshToken_shouldNotBeEqual_whenRefreshTokenDontExist() {
        UserEntity userEntity = UserEntity.builder()
                .id(330L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsy7mith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date("2003/10/10")) // Using LocalDate for clarity
                .role(Role.HOMEOWNER)
                .build();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(41L)
                .token(UUID.randomUUID().toString())
                .expiryDate(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES))) // Change to plus
                .user(userEntity)
                .build();

        when(userRepositoryMock.findByEmail("bobsy7mith@gmil.com")).thenReturn(Optional.of(userEntity));
        when(refreshTokenRepositoryMock.existsByEmail("bobsy7mith@gmil.com")).thenReturn(false);

        // Use ArgumentCaptor to capture the argument passed to save method
        ArgumentCaptor<RefreshTokenEntity> refreshTokenEntityCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        when(refreshTokenRepositoryMock.save(refreshTokenEntityCaptor.capture())).thenReturn(refreshTokenEntity);

        String refreshToken = refreshTokenUseCases.createRefreshToken("bobsy7mith@gmil.com");

        String expectedToken = refreshTokenEntityCaptor.getValue().getToken();

        assertEquals(expectedToken, refreshToken);

        verify(refreshTokenRepositoryMock).save(any(RefreshTokenEntity.class));
        verify(userRepositoryMock).findByEmail("bobsy7mith@gmil.com");
        verify(refreshTokenRepositoryMock).existsByEmail("bobsy7mith@gmil.com");

    }

    @Test
     void createRefreshToken_shouldBeEqual_whenRefreshTokenIsNotExpired() {
        UserEntity userEntity = UserEntity.builder()
                .id(330L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsy7mith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .build();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(41L)
                .token(UUID.randomUUID().toString())
                .expiryDate(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
                .user(userEntity)
                .build();
        when(refreshTokenRepositoryMock.existsByEmail("bobsy7mith@gmil.com"))
                .thenReturn(true);

        when(userRepositoryMock.findByEmail("bobsy7mith@gmil.com"))
                .thenReturn(Optional.of(userEntity));

        when(refreshTokenRepositoryMock.findByUserId(userEntity.getId()))
                .thenReturn(Optional.of(refreshTokenEntity));


        String actualResult = refreshTokenUseCases.createRefreshToken("bobsy7mith@gmil.com");

        String expectedResult = refreshTokenEntity.getToken();
        assertEquals(expectedResult, actualResult);

        verify(refreshTokenRepositoryMock).existsByEmail("bobsy7mith@gmil.com");
        verify(userRepositoryMock).findByEmail("bobsy7mith@gmil.com");
        verify(refreshTokenRepositoryMock).findByUserId(userEntity.getId());
        verify(refreshTokenRepositoryMock, never()).deleteById(refreshTokenEntity.getId());
    }
    @Test
     void createRefreshToken_shouldNotBeEqual_whenRefreshTokenIsExpired() {
        UserEntity userEntity = UserEntity.builder()
                .id(330L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsy7mith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .build();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                        .id(41L)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)))
                        .user(userEntity)
                        .build();
        when(refreshTokenRepositoryMock.existsByEmail("bobsy7mith@gmil.com"))
                .thenReturn(true);

        when(userRepositoryMock.findByEmail("bobsy7mith@gmil.com"))
                .thenReturn(Optional.of(userEntity));

        when(refreshTokenRepositoryMock.findByUserId(userEntity.getId()))
                .thenReturn(Optional.of(refreshTokenEntity));


        String actualResult = refreshTokenUseCases.createRefreshToken("bobsy7mith@gmil.com");

        String expectedResult = refreshTokenEntity.getToken();
        assertNotEquals(expectedResult, actualResult);

        verify(refreshTokenRepositoryMock).existsByEmail("bobsy7mith@gmil.com");
        verify(userRepositoryMock, times(2)).findByEmail("bobsy7mith@gmil.com");
        verify(refreshTokenRepositoryMock).findByUserId(userEntity.getId());
        verify(refreshTokenRepositoryMock).deleteById(refreshTokenEntity.getId());

    }

    @Test
     void renewAccessToken_shouldNotBeEqual_whenRefreshTokenIsExpired() {
        UserEntity userEntity = UserEntity.builder()
                .id(330L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsy7mith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .build();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(41L)
                .token(UUID.randomUUID().toString())
                .expiryDate(Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)))
                .user(userEntity)
                .build();
        RefreshTokenRequest request = RefreshTokenRequest.builder().refreshToken(refreshTokenEntity.getToken()).build();
        when(refreshTokenRepositoryMock.findByToken(request.getRefreshToken()))
                .thenReturn(Optional.of(refreshTokenEntity));

        when(userRepositoryMock.findByEmail("bobsy7mith@gmil.com"))
                .thenReturn(Optional.of(userEntity));


        RenewAccessTokenResponse actualResult = refreshTokenUseCases.renewAccessToken(request);

        RenewAccessTokenResponse expectedResult = RenewAccessTokenResponse.builder().refreshToken(refreshTokenEntity.getToken()).build();
        assertNotEquals(expectedResult, actualResult);

        verify(refreshTokenRepositoryMock).findByToken(request.getRefreshToken());
        verify(userRepositoryMock).findByEmail("bobsy7mith@gmil.com");
        verify(refreshTokenRepositoryMock).deleteById(refreshTokenEntity.getId());

    }
    @Test
    void renewAccessToken_shouldThrowNotFoundException_whenRefreshTokenIsExpired() {



        RefreshTokenRequest request = RefreshTokenRequest.builder().refreshToken("qwerty").build();
        when(refreshTokenRepositoryMock.findByToken("qwerty"))
                .thenReturn(Optional.empty());



        assertThrows(NotFoundException.class, () ->refreshTokenUseCases.renewAccessToken(request));

        verify(refreshTokenRepositoryMock).findByToken("qwerty");
        verify(userRepositoryMock, never()).findByEmail("bobsy7mith@gmil.com");
        verify(refreshTokenRepositoryMock, never()).deleteById(41L);

    }


}