package com.example.individualproject.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRequest {
    @NotNull
    private Long homeSeekerId;

    @NotNull
    private Long homeownerId;
}
