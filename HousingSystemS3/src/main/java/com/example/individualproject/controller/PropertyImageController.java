package com.example.individualproject.controller;

import com.example.individualproject.business.PropertyImageUseCases;
import com.example.individualproject.domain.CreatePropertyImageRequest;
import com.example.individualproject.domain.CreatePropertyImageResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/propertyImages")
@AllArgsConstructor
public class PropertyImageController {
    private final PropertyImageUseCases propertyImageUseCases;

    @RolesAllowed({"HOMEOWNER"})
    @PostMapping
    public ResponseEntity<CreatePropertyImageResponse> createProperty(
            @RequestPart("request") @Valid CreatePropertyImageRequest request,
            @RequestPart("file") MultipartFile image){
        request.setMultipartFile(image);
        CreatePropertyImageResponse response = propertyImageUseCases.createPropertyImage(request);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @RolesAllowed({"HOMEOWNER"})
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deleteProperty (@PathVariable(value = "id") long propertyImageId){
        propertyImageUseCases.deletePropertyImage(propertyImageId);
        return  ResponseEntity.noContent().build();
    }
}

