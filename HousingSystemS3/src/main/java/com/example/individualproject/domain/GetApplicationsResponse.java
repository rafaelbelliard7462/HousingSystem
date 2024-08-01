package com.example.individualproject.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetApplicationsResponse {
    private List<Application> applications;
}
