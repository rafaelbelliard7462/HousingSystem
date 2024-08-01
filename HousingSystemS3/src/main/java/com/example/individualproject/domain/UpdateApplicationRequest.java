package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationRequest {
    private  Long id;
    @NotNull
    private Date appliedDate;
    @NotNull
    private Status status;
    @NotBlank
    private  String description;
}
