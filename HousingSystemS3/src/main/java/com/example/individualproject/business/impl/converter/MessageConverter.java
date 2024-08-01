package com.example.individualproject.business.impl.converter;

import com.example.individualproject.domain.Chat;
import com.example.individualproject.domain.Message;
import com.example.individualproject.domain.User;
import com.example.individualproject.persistance.entity.ChatEntity;
import com.example.individualproject.persistance.entity.MessageEntity;
import com.example.individualproject.persistance.entity.UserEntity;

public class MessageConverter {
    private MessageConverter(){

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
    public static Chat chatConvert(ChatEntity chat) {
        return  Chat.builder()
                .id(chat.getId())
                .homeSeeker(userConvert(chat.getHomeSeeker()))
                .homeowner(userConvert(chat.getHomeowner()))
                .build();
    }
        public static Message convert(MessageEntity message) {
        return  Message.builder()
                .id(message.getId())
                .sender(userConvert(message.getSender()))
                .content(message.getContent())
                .chat(chatConvert(message.getChat()))
                .build();
    }
}
