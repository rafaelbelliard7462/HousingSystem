package com.example.individualproject.domain;

import com.example.individualproject.domain.enums.Status;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Application {
    private Long id;
    private User user;
    private  Property property;
    private Date appliedDate;
    private Status status;
    private  String description;

}
