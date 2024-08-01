package com.example.individualproject.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyImageRequest {
    @NotNull
    private Long propertyId;
    @NotBlank
    private String imageName;
    private MultipartFile multipartFile;

}
