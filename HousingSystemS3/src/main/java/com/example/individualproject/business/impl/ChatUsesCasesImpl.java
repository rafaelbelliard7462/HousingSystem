package com.example.individualproject.business.impl;

import com.example.individualproject.business.ChatUseCases;
import com.example.individualproject.business.exception.UnauthorizedDataAccessException;
import com.example.individualproject.business.impl.converter.ChatConverter;
import com.example.individualproject.configuration.security.token.AccessToken;
import com.example.individualproject.domain.Chat;
import com.example.individualproject.domain.CreateChatRequest;
import com.example.individualproject.persistance.ChatRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.ChatEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatUsesCasesImpl implements ChatUseCases {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private AccessToken requestAccessToken;
    @Override
    public Chat createChat(CreateChatRequest request) {

        if(chatRepository.existsByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId())){
            ChatEntity chatEntity = chatRepository.findByHomeowner_IdAndHomeSeeker_Id(request.getHomeownerId(), request.getHomeSeekerId()).orElseThrow();
            return ChatConverter.convert(chatEntity);
        }
        return ChatConverter.convert(saveChat(request));
    }



    @Override
    public List<Chat> getChatsByHomeSeekerId(Long user1Id) {
        if (!requestAccessToken.getUserId().equals(user1Id)) {
            throw new UnauthorizedDataAccessException("User_Id_Not_From_Logged_In_User");
        }

        return chatRepository.findAllByHomeSeeker_Id(user1Id).stream().map(ChatConverter ::convert).toList();
    }

    @Override
    public List<Chat> getChatsByHomeownerId(Long user2Id) {
        if (!requestAccessToken.getUserId().equals(user2Id)) {
            throw new UnauthorizedDataAccessException("User_Id_Not_From_Logged_In_User");
        }

        return chatRepository.findAllByHomeowner_Id(user2Id).stream().map(ChatConverter ::convert).toList();
    }

    public ChatEntity saveChat(CreateChatRequest request){
        UserEntity homeSeeker = userRepository.findById(request.getHomeSeekerId()).orElseThrow();
        UserEntity homeowner = userRepository.findById(request.getHomeownerId()).orElseThrow();
        ChatEntity chatEntity = ChatEntity.builder()
                .homeSeeker(homeSeeker)
                .homeowner(homeowner)
                .messages(new ArrayList<>())
                .build();
        return chatRepository.save(chatEntity);
    }



}
