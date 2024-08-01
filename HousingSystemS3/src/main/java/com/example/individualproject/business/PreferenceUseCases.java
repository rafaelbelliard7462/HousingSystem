package com.example.individualproject.business;

import com.example.individualproject.domain.CreatePreferenceRequest;
import com.example.individualproject.domain.CreatePreferenceResponse;
import com.example.individualproject.domain.Preference;
import com.example.individualproject.domain.UpdatePreferenceRequest;

import java.util.Optional;

public interface PreferenceUseCases {
    CreatePreferenceResponse createPreference(CreatePreferenceRequest request);
    Optional<Preference> getPreferenceByUserId(Long userId);
    void updatePreference(UpdatePreferenceRequest request);
    void deletePreference(Long preferenceId);
}
