package com.example.individualproject.business.impl;

import com.example.individualproject.business.PreferenceUseCases;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.business.impl.converter.PreferenceConverter;
import com.example.individualproject.domain.*;
import com.example.individualproject.persistance.PreferenceRepository;
import com.example.individualproject.persistance.entity.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PreferenceUsesCasesImpl implements PreferenceUseCases {
    private final PreferenceRepository preferenceRepository;

    @Transactional
    @Override
    public CreatePreferenceResponse createPreference(CreatePreferenceRequest request) {
        PreferenceEntity savedPreference = saveNewPreference(request);
        return CreatePreferenceResponse.builder()
                .preferenceId(savedPreference.getId())
                .build();
    }

    @Transactional
    @Override
    public Optional<Preference> getPreferenceByUserId(Long userId) {
        Optional <Preference> preference = preferenceRepository.findByUserId(userId).map(PreferenceConverter::convert);

        if(preference.isEmpty()){
            throw new NotFoundException("Preference_Not_Found");
        }
        return preference;

    }

    @Transactional
    @Override
    public void updatePreference(UpdatePreferenceRequest request) {
        Optional<PreferenceEntity> optionalPreferenceEntity = preferenceRepository.findById(request.getId());
        if(optionalPreferenceEntity.isEmpty()){
            throw new NotFoundException("Preference_Not_Found");
        }

        PreferenceEntity preference = optionalPreferenceEntity.get();

        preference.setCity(request.getCity());
        preference.setPrice(request.getPrice());
        preference.setRoom(request.getRoom());
        preference.setPropertyType(request.getPropertyType());

        preferenceRepository.save(preference);


    }

    @Transactional
    @Override
    public void deletePreference(Long preferenceId) {
        preferenceRepository.deleteById(preferenceId);
    }


    public PreferenceEntity saveNewPreference (CreatePreferenceRequest request) {
        PreferenceEntity preference = PreferenceEntity.builder()
                .user(getUserEntity(request.getUser()))
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .propertyType(request.getPropertyType())
                .build();
        return preferenceRepository.save(preference);
    }
    public UserEntity getUserEntity(User user){

        return  UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .build();
    }
}
