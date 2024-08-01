package com.example.individualproject.business.impl;

import com.example.individualproject.business.MessageUseCases;
import com.example.individualproject.business.impl.converter.MessageConverter;
import com.example.individualproject.domain.CreateMessageRequest;
import com.example.individualproject.domain.Message;
import com.example.individualproject.persistance.ChatRepository;
import com.example.individualproject.persistance.MessageRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.ChatEntity;
import com.example.individualproject.persistance.entity.MessageEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class MessageUseCasesImpl implements MessageUseCases {
    private  final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private  final UserRepository userRepository;

    @Override
    public Message createMessage(CreateMessageRequest request) {
        return MessageConverter.convert(saveMessage(request));
    }

    public MessageEntity saveMessage(CreateMessageRequest request){
        UserEntity userEntity = userRepository.findById(request.getSenderId()).orElseThrow();
        ChatEntity chatEntity = chatRepository.findById(request.getChatId()).orElseThrow();

       MessageEntity messageEntity= MessageEntity.builder()
                .sender(userEntity)
                .content(request.getContent())
                .chat(chatEntity)
                .build();
       chatEntity.getMessages().add(messageEntity);
        return messageRepository.save(messageEntity);
    }
}
