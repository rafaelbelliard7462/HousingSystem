package com.example.individualproject.business.impl;


import com.example.individualproject.business.exception.EmailAlreadyExist;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.business.exception.UnauthorizedDataAccessException;
import com.example.individualproject.configuration.security.token.AccessToken;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseImplTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private UserUseCasesImpl userUseCase;


    @Test
    void createUser_shouldBeEqual_whenTryingToCreateUser() {


        CreateUserRequest request = CreateUserRequest.builder()
                                    .firstName("Bob")
                                    .lastName("Smith")
                                    .email("bobsmith@gmil.com")
                                    .password("Bobby!")
                                    .dateOfBirth(new Date())
                                    .role(Role.HOMEOWNER)
                                    .build();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .dateOfBirth(request.getDateOfBirth())
                .role(request.getRole())
                .properties(List.of())
                .applications(List.of())
                .build();

        when(userRepositoryMock.save(any(UserEntity.class)))
                .thenReturn(userEntity);

        when(passwordEncoder.encode(userEntity.getPassword()))
                .thenReturn(String.valueOf(userEntity.getPassword()));

        CreateUserResponse actualResult=  userUseCase.createUser(request);

        User user = User.builder()
                .id(1L)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .dateOfBirth(request.getDateOfBirth())
                .role(request.getRole())
                .build();
        CreateUserResponse expectResult = CreateUserResponse.builder().userId(user.getId()).build();

        assertEquals(expectResult,actualResult);

         verify(userRepositoryMock).save(any(UserEntity.class));
         verify(passwordEncoder).encode(userEntity.getPassword());
    }

    @Test
    void createUser_shouldThrowEmailAlreadyExistException_whenTryingToCreateUserWithTheSameEmail() {


        CreateUserRequest request = CreateUserRequest.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .build();
        when(userRepositoryMock.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(EmailAlreadyExist.class, () ->userUseCase.createUser(request));

        verify(userRepositoryMock).existsByEmail(request.getEmail());
    }


    @Test
    void getUsers_shouldBeEqual_whenGettingAllTheUsers() {
        UserEntity userEntity = UserEntity.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        when(userRepositoryMock.findAll())
                .thenReturn(List.of(userEntity));

        GetUsersResponse actualResult = userUseCase.getUsers();
        User user = User.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();

        GetUsersResponse expectedResult = GetUsersResponse.builder()
                        .users(List.of(user))
                                .build();
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).findAll();
    }
    @Test
    void getUsers_shouldBeEqual_whenGettingAllTheUsersListIsEmpty() {

        when(userRepositoryMock.findAll())
                .thenReturn(List.of());

        GetUsersResponse actualResult = userUseCase.getUsers();
        GetUsersResponse expectedResult = GetUsersResponse.builder()
                .users(List.of())
                .build();

        assertEquals(expectedResult, actualResult);
        verify(userRepositoryMock).findAll();
    }


    @Test
    void getUserById_shouldBeEqual_whenGettingUserById() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        when(userRepositoryMock.findById(userEntity.getId()))
                .thenReturn(Optional.of(userEntity));

        when(accessToken.getUserId())
                .thenReturn(1L);

        Optional<User> optionalUser = userUseCase.getUserById(1L);
        User actualResult = optionalUser.get();

        User expectedResult = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).findById(userEntity.getId());
        verify(accessToken).getUserId();
    }

    @Test
    void getUserById_shouldThrowUnauthorizedDataAccessException_whenGettingOtherUserById() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        when(userRepositoryMock.findById(userEntity.getId()))
                .thenReturn(Optional.of(userEntity));

        when(accessToken.getUserId())
                .thenReturn(2L);


        assertThrows(UnauthorizedDataAccessException.class,()-> userUseCase.getUserById(1L));

        verify(userRepositoryMock).findById(userEntity.getId());
        verify(accessToken).getUserId();
    }


    @Test
    void getUserById_shouldThrowNotFoundException_whenGettingUserById() {

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class,() -> userUseCase.getUserById(1L));

        verify(userRepositoryMock).findById(1L);
    }

    @Test
    void getUserById_shouldBeEqual_whenGettingUserByEmail() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        when(userRepositoryMock.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));

        when(accessToken.getSubject())
                .thenReturn(userEntity.getEmail());

        Optional<User> optionalUser = userUseCase.getUserByEmail("bobsmith@gmil.com");
        User actualResult = optionalUser.get();

        User expectedResult = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).findByEmail(userEntity.getEmail());
        verify(accessToken).getSubject();
    }


    @Test
    void getUserByEmail_shouldThrowUnauthorizedDataAccessException_whenGettingOtherUserByEmail() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date(1,10,2003))
                .role(Role.HOMEOWNER)
                .properties(List.of())
                .applications(List.of())
                .build();
        when(userRepositoryMock.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));

        when(accessToken.getSubject())
                .thenReturn("random@gmail.com");


        assertThrows(UnauthorizedDataAccessException.class,() -> userUseCase.getUserByEmail(userEntity.getEmail()));

        verify(userRepositoryMock).findByEmail(userEntity.getEmail());
        verify(accessToken).getSubject();
    }

    @Test
    void getUserByEmail_shouldThrowNotFoundException_whenGettingUserByEmail() {

        when(userRepositoryMock.findByEmail("bobsmith@gmil.com"))
                .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class,() -> userUseCase.getUserByEmail("bobsmith@gmil.com"));

        verify(userRepositoryMock).findByEmail("bobsmith@gmil.com");
    }

    @Test
    void updateUser_shouldBeEqual_whenUpdatingUser() {

        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .dateOfBirth(request.getDateOfBirth())
                .role(request.getRole())
                .properties(List.of())
                .applications(List.of())
                .build();

        when(userRepositoryMock.findById(request.getId())).thenReturn(Optional.of(userEntity));

        when(accessToken.getUserId())
                .thenReturn(1L);

        userUseCase.updateUser(request);


        verify(userRepositoryMock).findById(request.getId());
        verify(userRepositoryMock).save(userEntity);
    }

    @Test
    void updateUser_shouldThrowUnauthorizedDataAccessException_whenUpdatingOtherUserById() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .dateOfBirth(request.getDateOfBirth())
                .role(request.getRole())
                .properties(List.of())
                .applications(List.of())
                .build();

        when(userRepositoryMock.findById(request.getId())).thenReturn(Optional.of(userEntity));

        when(accessToken.getUserId())
                .thenReturn(2L);


        assertThrows(UnauthorizedDataAccessException.class,()-> userUseCase.updateUser(request));

        verify(userRepositoryMock).findById(request.getId());
        verify(accessToken).getUserId();
    }
    @Test
    void updateUser_shouldThrowNotFoundException_whenUpdatingUserButTheUserIsNotFound() {

        UpdateUserRequest updatedRequest = UpdateUserRequest.builder()
                .id(1L)
                .firstName("Bobby")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .build();

        when(userRepositoryMock.findById(updatedRequest.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,() -> userUseCase.updateUser(updatedRequest));

        verify(userRepositoryMock).findById(updatedRequest.getId());
        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

    @Test
    void deleteProperty_ShouldVerifyIfDeleteById_WhenDeletingAProperty() {


        userUseCase.deleteUser(1L);

        verify(userRepositoryMock).deleteById(1L);
    }

}