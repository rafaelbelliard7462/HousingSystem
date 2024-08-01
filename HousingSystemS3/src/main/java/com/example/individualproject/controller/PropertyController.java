package com.example.individualproject.controller;

import com.example.individualproject.business.PropertyUseCases;
import com.example.individualproject.domain.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/properties")
@AllArgsConstructor

public class PropertyController {
    private  final PropertyUseCases propertyUseCase;

    @RolesAllowed({"HOMEOWNER"})
    @PostMapping
    public ResponseEntity<CreatePropertyResponse> createProperty(@RequestBody @Valid CreatePropertyRequest request){
        CreatePropertyResponse response = propertyUseCase.createProperty(request);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<GetPropertiesResponse> getProperties(){
        GetPropertiesResponse response = propertyUseCase.getProperties();
        return ResponseEntity.ok().body(response);
    }
    @RolesAllowed({"HOMEOWNER"})
    @GetMapping("/findPropertiesByUser/{id}")
    public ResponseEntity<GetPropertiesResponse> getPropertiesByUser(@PathVariable(value = "id") Long userId){
        GetPropertiesResponse response = propertyUseCase.getPropertiesByUser(userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Property> getPropertyId(@PathVariable(value = "id") long propertyId){
        Optional<Property> property = propertyUseCase.getPropertyById(propertyId);
        return property.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @RolesAllowed({"HOMEOWNER"})
    @PutMapping("{id}")
    public  ResponseEntity<Void> updateProperty(@PathVariable(value = "id") long propertyId,
                                                @RequestBody @Valid UpdatePropertyRequest request){
        request.setId(propertyId);
        propertyUseCase.updateProperty(request);

        return ResponseEntity.noContent().build();
    }
    @RolesAllowed({"HOMEOWNER"})
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deleteProperty (@PathVariable(value = "id") long propertyId){
        propertyUseCase.deleteProperty(propertyId);
        return  ResponseEntity.noContent().build();
    }


}
