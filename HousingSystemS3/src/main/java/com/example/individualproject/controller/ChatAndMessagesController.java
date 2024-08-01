package com.example.individualproject.controller;

import com.example.individualproject.business.ChatUseCases;
import com.example.individualproject.business.MessageUseCases;
import com.example.individualproject.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatAndMessagesController {
    private final ChatUseCases chatUseCases;
    private final MessageUseCases messageUseCases;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/createChat")
    //@SendTo("/queue/chat")
    public ResponseEntity<CreateChatResponse> createChat(@RequestBody CreateChatRequest request) {
        Chat chat = chatUseCases.createChat(request);
        CreateChatResponse response = CreateChatResponse.builder().chatId(chat.getId()).build();
        messagingTemplate.convertAndSendToUser(chat.getHomeowner().getEmail(), "/queue/chat", chat);
        messagingTemplate.convertAndSendToUser(chat.getHomeSeeker().getEmail(), "/queue/chat", chat);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/homeSeeker/{userId}/chats")
    public ResponseEntity<List<Chat>> getChatsByHomeSeekerId(@PathVariable Long userId) {
        List<Chat> chats = chatUseCases.getChatsByHomeSeekerId(userId);
        return ResponseEntity.ok().body(chats);
    }
    @GetMapping("/homeowner/{userId}/chats")
    public ResponseEntity<List<Chat>> getChatsByHomeownerId(@PathVariable Long userId) {
        List<Chat> chats = chatUseCases.getChatsByHomeownerId(userId);
        return ResponseEntity.ok().body(chats);
    }
    @PostMapping("/sendMessage")
    //@SendTo("/queue/chat/{chatId}/messages")
    public ResponseEntity<CreateMessageResponse> createMessage( @RequestBody CreateMessageRequest request) {
        Message message = messageUseCases.createMessage(request);
        CreateMessageResponse response = CreateMessageResponse.builder().messageId(message.getId()).build();
        messagingTemplate.convertAndSendToUser(request.getChatId().toString() , "/queue/messages", message);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
