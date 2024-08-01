package com.example.individualproject.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PropertyImage {
    private Long id;
    private Property property;
    private String imageName;
    private String imageUrl;
    private String fileName;

}
