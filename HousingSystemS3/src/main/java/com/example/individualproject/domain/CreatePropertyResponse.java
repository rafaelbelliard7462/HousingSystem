package com.example.individualproject.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePropertyResponse {
    private long propertyId;
}
