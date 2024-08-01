package com.example.individualproject.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMessageResponse {
    private Long messageId;
}
