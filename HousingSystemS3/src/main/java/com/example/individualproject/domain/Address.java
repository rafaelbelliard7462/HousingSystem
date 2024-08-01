package com.example.individualproject.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Address {
    private Long id;
    private String street;
    private String city;
    private String postCode;

}
