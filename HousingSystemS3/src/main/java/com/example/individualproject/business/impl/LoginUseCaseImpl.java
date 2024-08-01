package com.example.individualproject.business.impl;

import com.example.individualproject.business.LoginUseCase;
import com.example.individualproject.business.RefreshTokenUseCase;
import com.example.individualproject.business.exception.InvalidCredentialsException;
import com.example.individualproject.configuration.security.token.AccessTokenEncoder;
import com.example.individualproject.configuration.security.token.impl.AccessTokenImpl;
import com.example.individualproject.domain.LoginRequest;
import com.example.individualproject.domain.LoginResponse;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private  final RefreshTokenUseCase refreshTokenUseCase;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
       Optional <UserEntity> optionalUserEntity = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUserEntity.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        UserEntity user = optionalUserEntity.get();
        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = refreshTokenUseCase.createRefreshToken(user.getEmail());
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
       Long userId = 0L;

        String role =  " ";

        String email = " ";

        return getString(user, userId, role, email, accessTokenEncoder);
    }

    static String getString(UserEntity user, Long userId, String role, String email, AccessTokenEncoder accessTokenEncoder) {
        if (user != null){
            userId = user.getId();
            role =  user.getRole().toString();
            email = user.getEmail();
        }

        Set<String> roles= Set.of( role);

        return accessTokenEncoder.encode(
                new AccessTokenImpl( email, userId, roles));
    }

}
