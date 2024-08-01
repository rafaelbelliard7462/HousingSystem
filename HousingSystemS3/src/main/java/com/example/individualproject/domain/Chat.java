package com.example.individualproject.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Chat {

    private  Long id;

    private User homeSeeker;

    private User homeowner;

    @Builder.Default
    private List<Message> messages = new ArrayList<>();
}
