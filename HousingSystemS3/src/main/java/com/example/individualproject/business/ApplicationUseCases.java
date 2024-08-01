package com.example.individualproject.business;

import com.example.individualproject.domain.*;

import java.util.Optional;

public interface ApplicationUseCases {
    CreateApplicationResponse createApplication (CreateApplicationRequest request);

    GetApplicationsResponse getApplicationsByUser(Long userId);
    GetApplicationsResponse getApplicationsByProperty(Long propertyId);
    Optional<Application> getApplicationById (Long applicationId);
    void updateApplication (UpdateApplicationRequest request);
    void deleteApplication (Long applicationId);
}
