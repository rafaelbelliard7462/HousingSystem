package com.example.individualproject.business.impl;

import com.example.individualproject.business.UserUseCases;
import com.example.individualproject.business.exception.EmailAlreadyExist;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.business.exception.UnauthorizedDataAccessException;
import com.example.individualproject.business.impl.converter.UserConverter;
import com.example.individualproject.configuration.security.token.AccessToken;
import com.example.individualproject.domain.*;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserUseCasesImpl implements UserUseCases {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private AccessToken requestAccessToken;
    @Transactional
    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw  new EmailAlreadyExist();
        }

        UserEntity savedUser = saveNewUser(request);
        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    @Transactional
    @Override
    public GetUsersResponse getUsers() {
        List<User> users = userRepository.findAll()
                .stream()
                .map(UserConverter::convert)
                .toList();

        return GetUsersResponse.builder()
                .users(users)
                .build();
    }
    @Transactional
    @Override
    public Optional<User> getUserById(Long userId) {
        Optional<User> user =  userRepository.findById(userId).map(UserConverter ::convert);
        if(user.isEmpty()){
            throw  new NotFoundException("User_Not_Found");
        }
        if (!requestAccessToken.getUserId().equals(userId)) {
            throw new UnauthorizedDataAccessException("User_Id_Not_From_Logged_In_User");
        }
        return user;

    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email).map(UserConverter ::convert);
        if(user.isEmpty()){
            throw  new NotFoundException("Property_Not_Found");
        }
       if (!requestAccessToken.getSubject().equals(email)) {
            throw new UnauthorizedDataAccessException("User_Email_Not_From_Logged_In_User");
        }
        return user;
    }
    @Transactional
    @Override
    public void updateUser(UpdateUserRequest request) {
        Optional<UserEntity> userOptional = userRepository.findById(request.getId());
        if(userOptional.isEmpty()){
            throw new NotFoundException("NOT_FOUND");
        }
        if (!requestAccessToken.getUserId().equals(request.getId())) {
            throw new UnauthorizedDataAccessException("User_Id_Not_From_Logged_In_User");
        }
        UserEntity user = userOptional.get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private  UserEntity saveNewUser(CreateUserRequest request){

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .password(encodedPassword)
                .role(request.getRole())
                .build();
        return  userRepository.save(user);
    }
}
