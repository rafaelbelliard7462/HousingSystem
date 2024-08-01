package com.example.individualproject.business.impl;

import com.example.individualproject.business.RefreshTokenUseCase;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.configuration.security.token.AccessTokenEncoder;
import com.example.individualproject.domain.RefreshTokenRequest;
import com.example.individualproject.domain.RenewAccessTokenResponse;
import com.example.individualproject.persistance.RefreshTokenRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.RefreshTokenEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.individualproject.business.impl.LoginUseCaseImpl.getString;

@Service
@AllArgsConstructor
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {
    private final AccessTokenEncoder accessTokenEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public String createRefreshToken(String email) {
        if(refreshTokenRepository.existsByEmail(email)){
            UserEntity userEntity = userRepository.findByEmail(email).get();
            RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUserId(userEntity.getId()).get();
            if(refreshTokenEntity.getExpiryDate().compareTo(Date.from(Instant.now()))<0){
                refreshTokenRepository.deleteById(refreshTokenEntity.getId());

            }
            else{
                return refreshTokenEntity.getToken();
            }


        }
       return saveRefreshToken(email);
    }

    @Override
    public RenewAccessTokenResponse renewAccessToken(RefreshTokenRequest request) {
        Optional<RefreshTokenEntity> refreshTokenEntityOptional = refreshTokenRepository.findByToken(request.getRefreshToken());
        if(refreshTokenEntityOptional.isEmpty()){
            throw  new NotFoundException("REFRESH_TOKEN_NOT_FOUND");
        }
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityOptional.get();

        String refreshToken = refreshTokenEntity.getToken();
        if(refreshTokenEntity.getExpiryDate().compareTo(Date.from(Instant.now()))<0){
            refreshTokenRepository.deleteById(refreshTokenEntity.getId());
            refreshToken = createRefreshToken(refreshTokenEntity.getUser().getEmail());

        }


       String accessToken =generateAccessToken(refreshTokenEntity.getUser());
        return RenewAccessTokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }


    private String generateAccessToken(UserEntity user) {
        Long userId = 0L;

        String role =  "";

        String email = "";

        return getString(user, userId, role, email, accessTokenEncoder);
    }
    private String saveRefreshToken( String email){

        Instant now =Instant.now();
        UserEntity userEntity= userRepository.findByEmail(email).orElseThrow();
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userEntity)
                .token(UUID.randomUUID().toString())
                .expiryDate(Date.from(now.plus(2, ChronoUnit.MINUTES)))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return  refreshTokenEntity.getToken();
    }
}
