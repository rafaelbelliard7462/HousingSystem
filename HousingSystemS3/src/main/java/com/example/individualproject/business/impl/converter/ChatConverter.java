package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.Chat;
import com.example.individualproject.domain.User;
import com.example.individualproject.persistance.entity.ChatEntity;
import com.example.individualproject.persistance.entity.UserEntity;

public class ChatConverter {
    private ChatConverter(){

    }
    public static User userConvert(UserEntity userEntity) {
        return  User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .dateOfBirth(userEntity.getDateOfBirth())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }
    public static Chat convert(ChatEntity chat) {
        return  Chat.builder()
                .id(chat.getId())
                .homeSeeker(userConvert(chat.getHomeSeeker()))
                .homeowner(userConvert(chat.getHomeowner()))
                .messages(chat.getMessages().stream().map(MessageConverter::convert).toList())
                .build();
    }
}
