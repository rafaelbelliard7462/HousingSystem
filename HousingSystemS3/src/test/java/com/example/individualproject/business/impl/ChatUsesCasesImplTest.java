package com.example.individualproject.business.impl;

import com.example.individualproject.business.exception.UnauthorizedDataAccessException;
import com.example.individualproject.configuration.security.token.AccessToken;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.ChatRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.ChatEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatUsesCasesImplTest {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private ChatUsesCasesImpl chatUsesCases;

    CreateChatRequest CreateChatObject(){


        return CreateChatRequest.builder()
                .homeownerId(createUser().getId())
                .homeSeekerId(createUser1().getId())
                .build();


    }
    User createUser() {
        return User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(new ArrayList<>())
                .build();
    }
    User createUser1() {
        return User.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(new Date())
                .role(Role.HOMEOWNER)
                .properties(new ArrayList<>())
                .build();
    }
    public UserEntity getUserEntity(User user){

        return  UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .properties(new ArrayList<>())
                .build();
    }


    @Test
    void createChat_shouldBeEqual_whenCreateMessage() {

        CreateChatRequest request = CreateChatObject();
        User user = createUser();
        User user1 = createUser1();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .homeowner(getUserEntity(user))
                .homeSeeker(getUserEntity(user1))
                .messages(new ArrayList<>())
                .build();


        when(chatRepository.existsByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId()))
                .thenReturn(false);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(getUserEntity(user)));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(getUserEntity(user1)));


        when(chatRepository.save(any(ChatEntity.class)))
                .thenReturn(chatEntity);

        Chat actualResult = chatUsesCases.createChat(request);


        Chat expectedResult = Chat.builder()
                .id(1L)
                .homeowner(user)
                .homeSeeker(user1)
                .messages(new ArrayList<>())
                .build();
        assertEquals(expectedResult,actualResult);
        verify(chatRepository).existsByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId());
        verify(chatRepository, never()).findByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId());
        verify(chatRepository).save(any(ChatEntity.class));
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);

    }
    @Test
    void createChat_shouldBeEqual_whenChatExist() {

        CreateChatRequest request = CreateChatObject();
        User user = createUser();
        User user1 = createUser1();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .homeowner(getUserEntity(user))
                .homeSeeker(getUserEntity(user1))
                .messages(new ArrayList<>())
                .build();

        when(chatRepository.existsByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId()))
                .thenReturn(true);

        when(chatRepository.findByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId()))
                .thenReturn(Optional.of(chatEntity));



        Chat actualResult = chatUsesCases.createChat(request);


        Chat expectedResult = Chat.builder()
                .id(1L)
                .homeowner(user)
                .homeSeeker(user1)
                .messages(new ArrayList<>())
                .build();
        assertEquals(expectedResult,actualResult);

        verify(chatRepository).existsByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId());
        verify(chatRepository).findByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId());
        verify(chatRepository,never()).save(any(ChatEntity.class));
        verify(userRepository,never()).findById(1L);
        verify(userRepository,never()).findById(2L);

    }


    @Test
    void getChatsByHomeSeekerId_shouldBeEqual_whenGettingChatsByHomeSeekerId() {

        User user = createUser();
        User user1 = createUser1();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .homeowner(getUserEntity(user))
                .homeSeeker(getUserEntity(user1))
                .messages(new ArrayList<>())
                .build();

        when(accessToken.getUserId())
                .thenReturn(2L);
        when(chatRepository.findAllByHomeSeeker_Id(2L))
                .thenReturn(List.of(chatEntity));

        List<Chat> actualResult = chatUsesCases.getChatsByHomeSeekerId(2L);

        Chat chat = Chat.builder()
                .id(1L)
                .homeowner(user)
                .homeSeeker(user1)
                .messages(new ArrayList<>())
                .build();
        assertEquals(List.of(chat),actualResult);
        verify(chatRepository).findAllByHomeSeeker_Id(2L);
        verify(accessToken).getUserId();
    }
    @Test
    void getChatsByHomeSeekerId_shouldThrowUnauthorizedDataAccessException_whenGettingOtherChatsByHomeSeekerId() {

        when(accessToken.getUserId())
                .thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class,()-> chatUsesCases.getChatsByHomeSeekerId(3L));
        verify(chatRepository, never()).findAllByHomeSeeker_Id(2L);
        verify(accessToken).getUserId();
    }

    @Test
    void getChatsByHomeownerId_shouldBeEqual_whenGettingChatsByHomeownerId() {

        User user = createUser();
        User user1 = createUser1();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .homeowner(getUserEntity(user))
                .homeSeeker(getUserEntity(user1))
                .messages(new ArrayList<>())
                .build();

        when(accessToken.getUserId())
                .thenReturn(1L);
        when(chatRepository.findAllByHomeowner_Id(1L))
                .thenReturn(List.of(chatEntity));

        List<Chat> actualResult = chatUsesCases.getChatsByHomeownerId(1L);

        Chat chat = Chat.builder()
                .id(1L)
                .homeowner(user)
                .homeSeeker(user1)
                .messages(new ArrayList<>())
                .build();
        assertEquals(List.of(chat),actualResult);
        verify(chatRepository).findAllByHomeowner_Id(1L);
        verify(accessToken).getUserId();
    }
    @Test
    void getChatsByHomeownerId_shouldThrowUnauthorizedDataAccessException_whenGettingOtherChatsByHomeownerId() {

        when(accessToken.getUserId())
                .thenReturn(1L);


        assertThrows(UnauthorizedDataAccessException.class,()-> chatUsesCases.getChatsByHomeownerId(3L));
        verify(chatRepository, never()).findAllByHomeowner_Id(1L);
        verify(accessToken).getUserId();
    }


}