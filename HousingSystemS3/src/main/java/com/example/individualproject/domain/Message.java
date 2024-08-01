package com.example.individualproject.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Message {

    private  Long id;

    private String content;

    private User sender;

    private Chat chat;

}
