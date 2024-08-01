package com.example.individualproject.controller;

import com.example.individualproject.business.PreferenceUseCases;
import com.example.individualproject.domain.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/preference")
@AllArgsConstructor
public class PreferenceController {
    private final PreferenceUseCases preferenceUseCases;
    @RolesAllowed({"HOME_SEEKER"})
    @PostMapping
    public ResponseEntity<CreatePreferenceResponse> createPreference(@RequestBody @Valid CreatePreferenceRequest request){
        CreatePreferenceResponse response = preferenceUseCases.createPreference(request);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed({"HOME_SEEKER"})
    @GetMapping("/id/{id}")
    public ResponseEntity<Preference> getPreferenceByUserId(@PathVariable(value = "id") Long userId){
        Optional<Preference> preference = preferenceUseCases.getPreferenceByUserId(userId);
        return preference.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());

    }
    @RolesAllowed({"HOME_SEEKER"})
    @PutMapping("{id}")
    public  ResponseEntity<Void> updatePreference(@PathVariable(value = "id") Long preferenceId,
                                                   @RequestBody @Valid UpdatePreferenceRequest request){
        request.setId(preferenceId);
        preferenceUseCases.updatePreference(request);

        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"HOME_SEEKER"})
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deletePreference (@PathVariable(value = "id") Long preferenceId){
        preferenceUseCases.deletePreference(preferenceId);
        return  ResponseEntity.noContent().build();
    }
}
