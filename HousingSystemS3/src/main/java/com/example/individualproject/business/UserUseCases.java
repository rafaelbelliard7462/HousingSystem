package com.example.individualproject.business;

import com.example.individualproject.domain.*;

import java.util.Optional;

public interface UserUseCases {
    CreateUserResponse createUser (CreateUserRequest request);
    GetUsersResponse getUsers();
    Optional<User> getUserById (Long userId);
    Optional<User> getUserByEmail (String email);
    void updateUser (UpdateUserRequest request);
    void  deleteUser (Long userId);
}
