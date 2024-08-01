package com.example.individualproject.controller;

import com.example.individualproject.business.ApplicationUseCases;
import com.example.individualproject.domain.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/applications")
@AllArgsConstructor
public class ApplicationController {
    private final ApplicationUseCases applicationUseCases;
    @RolesAllowed({"HOME_SEEKER"})
    @PostMapping
    public ResponseEntity<CreateApplicationResponse> createProperty(@RequestBody @Valid CreateApplicationRequest request){
        CreateApplicationResponse response = applicationUseCases.createApplication(request);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed({"HOMEOWNER"})
    @GetMapping("/findApplicationsByProperty/{id}")
    public ResponseEntity<GetApplicationsResponse> getApplicationsByProperty(@PathVariable(value = "id") Long propertyId){
        GetApplicationsResponse response = applicationUseCases.getApplicationsByProperty(propertyId);
        return ResponseEntity.ok().body(response);
    }
    @RolesAllowed({"HOME_SEEKER"})
    @GetMapping("/findApplicationsByUser/{id}")
    public ResponseEntity<GetApplicationsResponse> getApplicationsByUser(@PathVariable(value = "id") Long userId){
        GetApplicationsResponse response = applicationUseCases.getApplicationsByUser(userId);
        return ResponseEntity.ok().body(response);
    }

    @RolesAllowed({"HOME_SEEKER", "HOMEOWNER"})
    @GetMapping("/id/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable(value = "id") Long propertyId){
        Optional<Application> application = applicationUseCases.getApplicationById(propertyId);
        return application.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());

    }
    @RolesAllowed({"HOMEOWNER"})
    @PutMapping("{id}")
    public  ResponseEntity<Void> updateApplication(@PathVariable(value = "id") Long propertyId,
                                                   @RequestBody @Valid UpdateApplicationRequest request){
        request.setId(propertyId);
        applicationUseCases.updateApplication(request);

        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"HOMEOWNER"})
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deleteProperty (@PathVariable(value = "id") Long applicationId){
        applicationUseCases.deleteApplication(applicationId);
        return  ResponseEntity.noContent().build();
    }

}
