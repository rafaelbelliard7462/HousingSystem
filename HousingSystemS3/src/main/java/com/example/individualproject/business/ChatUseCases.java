package com.example.individualproject.business;

import com.example.individualproject.domain.Chat;
import com.example.individualproject.domain.CreateChatRequest;

import java.util.List;

public interface ChatUseCases {
    Chat createChat(CreateChatRequest request);
    List<Chat> getChatsByHomeSeekerId(Long user1Id);
    List<Chat> getChatsByHomeownerId(Long user2Id);

}
