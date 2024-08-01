package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferenceRequest {
    private Long id;

    @NotNull
    private User user;

    @NotBlank
    private String city;

    @NotNull
    private PropertyType propertyType;

    @NotNull
    private  double price;

    @NotNull
    private  int room;
}
