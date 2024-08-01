package com.example.individualproject.business.impl;

import com.example.individualproject.business.ApplicationUseCases;
import com.example.individualproject.business.exception.AlreadyExistException;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.business.impl.converter.ApplicationConverter;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.ApplicationRepository;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class ApplicationUseCasesImpl implements ApplicationUseCases {
    private final ApplicationRepository applicationRepository;
    private  final UserRepository userRepository;
    private  final PropertyRepository propertyRepository;
    @Override
    public CreateApplicationResponse createApplication(CreateApplicationRequest request) {
        if(applicationRepository.existsByUser_IdAndProperty_Id(request.getUser().getId(), request.getProperty().getId())){
            throw new AlreadyExistException("APPLICATION_ALREADY_EXIST");
        }

        ApplicationEntity savedApplication = saveApplication(request);

        return CreateApplicationResponse.builder().applicationId(savedApplication.getId()).build();
    }



    @Override
    public GetApplicationsResponse getApplicationsByUser(Long userId) {
        return GetApplicationsResponse.builder()
                .applications(applicationRepository.findAllByUserId(userId)
                                    .stream()
                                    .map(ApplicationConverter ::convert).toList())
                                    .build();
    }

    @Override
    public GetApplicationsResponse getApplicationsByProperty(Long propertyId) {

        return GetApplicationsResponse.builder()
                .applications(applicationRepository.findAllByPropertyId(propertyId)
                        .stream()
                        .map(ApplicationConverter ::convert).toList())
                .build();
    }

    @Override
    public Optional<Application> getApplicationById(Long applicationId) {
        Optional<Application> application = applicationRepository.findById(applicationId).map(ApplicationConverter ::convert);
        if(application.isEmpty()){
            throw new NotFoundException("Application_Not_Found");
        }
        return application;
    }

    @Override
    public void updateApplication(UpdateApplicationRequest request) {
        Optional<ApplicationEntity> optionalApplicationEntity = applicationRepository.findById(request.getId());

        if(optionalApplicationEntity.isEmpty()){
            throw new NotFoundException("Application_Not_Found");
        }

        ApplicationEntity application = optionalApplicationEntity.get();

        application.setUser(application.getUser());
        application.setProperty(application.getProperty());
        application.setAppliedDate(request.getAppliedDate());
        if (Status.ACCEPTED.equals(request.getStatus())) {
            // Set all other applications with the same property ID to DECLINED
            PropertyEntity propertyEntity = propertyRepository.findById(application.getProperty().getId()).orElseThrow();
            List<ApplicationEntity> otherApplications =propertyEntity.getApplications();

            propertyEntity.setRentedDate(new Date());
            propertyEntity.setRented(true);
            propertyRepository.save(propertyEntity);
            otherApplications.forEach(otherApplication -> {
                otherApplication.setStatus(Status.DECLINED);
                applicationRepository.save(otherApplication);
            });

        }
        application.setStatus(request.getStatus());
        application.setDescription(request.getDescription());

        applicationRepository.save(application);
    }

    @Override
    public void deleteApplication(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }

    public ApplicationEntity saveApplication(CreateApplicationRequest request){
        PropertyEntity propertyEntity = propertyRepository.findById(request.getProperty().getId()).orElseThrow();
        UserEntity userEntity = userRepository.findById(request.getUser().getId()).orElseThrow();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .user(userEntity)
                .property(propertyEntity)
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        propertyEntity.getApplications().add(applicationEntity);
        userEntity.getApplications().add(applicationEntity);
        return  applicationRepository.save(applicationEntity);

    }

}
