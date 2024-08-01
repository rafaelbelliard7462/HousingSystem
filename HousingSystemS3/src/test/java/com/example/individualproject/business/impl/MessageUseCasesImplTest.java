package com.example.individualproject.business.impl;

import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.persistance.ChatRepository;
import com.example.individualproject.persistance.MessageRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.ChatEntity;
import com.example.individualproject.persistance.entity.MessageEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageUseCasesImplTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private  ChatRepository chatRepository;
    @Mock
    private   UserRepository userRepository;
    @InjectMocks
    private MessageUseCasesImpl messageUseCases;

    CreateMessageRequest CreateMessageObject(){


        return CreateMessageRequest.builder()
                .senderId(1L)
                .content("Hello")
                .chatId(2L)
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
    Chat createChat() {
        return Chat.builder()
                .id(2L)
                .homeowner(createUser())
                .homeSeeker(createUser())
                .messages(new ArrayList<>())
                .build();
    }
    public ChatEntity getChatEntity(Chat chat){

        return  ChatEntity.builder()
                .id(chat.getId())
                .homeowner(getUserEntity(chat.getHomeowner()))
                .homeSeeker(getUserEntity(chat.getHomeSeeker()))
                .messages(new ArrayList<>())
                .build();
    }
    @Test
    void createMessage_shouldBeEqual_WhenCreateMessage() {

        CreateMessageRequest request = CreateMessageObject();
        User user= createUser();
        Chat chat= createChat();
        UserEntity userEntity = getUserEntity(user);
        ChatEntity chatEntity = getChatEntity(chat);
        MessageEntity messageEntity = MessageEntity.builder()
                .id(1L)
                .sender(userEntity)
                .content(request.getContent())
                .chat(chatEntity)
                .build();
        chatEntity.getMessages().add(messageEntity);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userEntity));

        when(chatRepository.findById(2L))
                .thenReturn(Optional.of(chatEntity));


        when(messageRepository.save(any(MessageEntity.class)))
                .thenReturn(messageEntity);

        Message actualResult = messageUseCases.createMessage(request);


        Message expectedResult = Message.builder()
                .id(1L)
                .sender(user)
                .content(request.getContent())
                .chat(chat)
                .build();
        assertEquals(expectedResult,actualResult);
        verify(messageRepository).save(any(MessageEntity.class));

    }



}