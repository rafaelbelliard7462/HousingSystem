package com.example.individualproject.business;

import com.example.individualproject.domain.CreateMessageRequest;
import com.example.individualproject.domain.Message;


public interface MessageUseCases {
    Message createMessage(CreateMessageRequest request);
}
