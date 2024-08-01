package com.example.individualproject.business.impl;

import com.example.individualproject.business.PropertyAlertUseCases;
import com.example.individualproject.business.PropertyUseCases;
import com.example.individualproject.business.exception.AlreadyExistException;
import com.example.individualproject.business.exception.NotFoundException;
import com.example.individualproject.business.impl.converter.PropertyConverter;
import com.example.individualproject.domain.*;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.AddressEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropertyUseCasesImpl implements PropertyUseCases {
    private final PropertyRepository propertyRepository;
    private  final UserRepository userRepository;
    private final PropertyAlertUseCases propertyAlertUseCases;
    @Transactional
    @Override
    public CreatePropertyResponse createProperty(CreatePropertyRequest request) {
        if (propertyRepository.existsByStreet(request.getAddress().getStreet())) {
            throw new AlreadyExistException("Property_Already_Exist");
        }

        PropertyEntity savedProperty = saveNewProperty(request);
        propertyAlertUseCases.sendPropertyAlertToUser(savedProperty);
        return CreatePropertyResponse.builder()
                .propertyId(savedProperty.getId())
                .build();
    }

    @Transactional
    @Override
    public GetPropertiesResponse getProperties() {
         return GetPropertiesResponse.builder()
                .properties(this.propertyRepository.findAll()
                        .stream()
                        .map(PropertyConverter:: convert).toList())
                .build();
    }



    @Transactional
    @Override
    public GetPropertiesResponse getPropertiesByUser(Long userId) {
        List<PropertyEntity> propertyEntities = propertyRepository.findAllByUserId(userId);

        return GetPropertiesResponse.builder()
                .properties(propertyEntities
                        .stream()
                        .map(PropertyConverter :: convert).toList())
                .build();
    }
    @Transactional
    @Override
    public Optional<Property> getPropertyById(Long propertyId) {
        Optional <Property> property = propertyRepository.findById(propertyId) .map(PropertyConverter ::convert);
        if(property.isEmpty()){
            throw  new NotFoundException("Property_Not_Found");
        }
        return property;
    }
    @Transactional
    @Override
    public void updateProperty(UpdatePropertyRequest request) {
        Optional<PropertyEntity> optionalProperty = propertyRepository.findById(request.getId());
        if(optionalProperty.isEmpty()){
            throw  new NotFoundException("Property_Not_Found");
        }

        PropertyEntity property = optionalProperty.get();


        // Fetch the existing AddressEntity
        AddressEntity existingAddress = property.getAddress();

        // Update the existing AddressEntity with the new values
        existingAddress.setStreet(request.getAddress().getStreet());
        existingAddress.setCity(request.getAddress().getCity());
        existingAddress.setPostCode(request.getAddress().getPostCode());

        UserEntity userEntity = userRepository.findById(request.getUserId()).orElseThrow();
        property.setUser(userEntity);
        property.setAvailable(request.getAvailable());
        property.setRoom(request.getRoom());
        property.setPrice(request.getPrice());
        property.setPropertyType(request.getPropertyType());
        property.setDescription(request.getDescription());

        propertyRepository.save(property);
    }
    @Transactional
    @Override
    public void deleteProperty(Long propertyId) {
        propertyRepository.deleteById(propertyId);

    }

    public PropertyEntity saveNewProperty (CreatePropertyRequest request){
        UserEntity userEntity = userRepository.findById(request.getUserId()).orElseThrow();
        PropertyEntity property = PropertyEntity.builder()
                .user(userEntity)
                .price(request.getPrice())
                .address(getAddressEntity(request.getAddress()))
                .room(request.getRoom())
                .available(request.getAvailable())
                .propertyType(request.getPropertyType())
                .description(request.getDescription())
                .build();
        userEntity.getProperties().add(property);
        return  propertyRepository.save(property);
    }


    public AddressEntity getAddressEntity(Address address){
        return  AddressEntity.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .postCode(address.getPostCode())
                .build();
    }

}
