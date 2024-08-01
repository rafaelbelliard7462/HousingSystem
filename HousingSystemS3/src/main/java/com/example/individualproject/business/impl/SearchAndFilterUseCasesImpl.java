package com.example.individualproject.business.impl;

import com.example.individualproject.business.SearchAndFilterUseCases;
import com.example.individualproject.business.exception.UnauthorizedDataAccessException;
import com.example.individualproject.business.impl.converter.ApplicationConverter;
import com.example.individualproject.business.impl.converter.PropertyConverter;
import com.example.individualproject.configuration.security.token.AccessToken;
import com.example.individualproject.domain.Application;
import com.example.individualproject.domain.Property;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Status;
import com.example.individualproject.persistance.ApplicationRepository;
import com.example.individualproject.persistance.PropertyRepository;
import com.example.individualproject.persistance.UserRepository;
import com.example.individualproject.persistance.entity.ApplicationEntity;
import com.example.individualproject.persistance.entity.PropertyEntity;
import com.example.individualproject.persistance.specification.ApplicationSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchAndFilterUseCasesImpl implements SearchAndFilterUseCases {
    private final ApplicationRepository applicationRepository;
    private  final PropertyRepository propertyRepository;
    private  final UserRepository userRepository;
    private AccessToken requestAccessToken;
    @Override
    public Page<Application> getApplicationsByUser(Long userId, Status status, Pageable pageable) {
        Page<ApplicationEntity> applicationsPage = applicationRepository.findByProperty_User_IdAndStatus(userId, status, pageable);
        List<Application> applications = applicationsPage.getContent().stream().map(ApplicationConverter::convert).toList();
        return new PageImpl<>(applications, pageable, applicationsPage.getTotalElements());
    }
    @Override
    public Page<Application> searchApplicationsFromUser(Long userId, Status status, String searchString, Pageable pageable) {
        ApplicationSpecification spec = new ApplicationSpecification(userId, status, searchString);
        Page<ApplicationEntity> applicationsPage =applicationRepository.findAll(spec, pageable);
        List<Application> applications = applicationsPage.getContent().stream().map(ApplicationConverter::convert).toList();
        return new PageImpl<>(applications, pageable, applicationsPage.getTotalElements());
    }

    @Override
    public Page<Property> searchProperties(String city, double maxPrice, int minSize, PropertyType propertyType, boolean rented, Pageable pageable) {
        Page<PropertyEntity> propertiesPage;
        if (city == null && maxPrice == 0.0 && minSize == 0 && propertyType == null && !rented) {
            propertiesPage = propertyRepository.findAllByRented(false,pageable);
        }
        else {
            if(maxPrice == 0.0){
                maxPrice  = Double.MAX_VALUE;
            }


            propertiesPage = propertyRepository.findByCityAndPriceAndSize(city, maxPrice, minSize, propertyType, rented, pageable);
        }

        List<Property> properties = propertiesPage.getContent().stream().map(PropertyConverter::convert).toList();
        return new PageImpl<>(properties, pageable, propertiesPage.getTotalElements());

    }

    @Override
    public Page<Property> searchHomeownerProperties(String city, double maxPrice, int minSize, PropertyType propertyType, boolean rented, Long userId, Pageable pageable) {
        if (!requestAccessToken.getUserId().equals(userId)) {
            throw new UnauthorizedDataAccessException("User_Id_Not_From_Logged_In_User");
        }
        Page<PropertyEntity> propertiesPage;
        if (city == null && maxPrice == 0.0 && minSize == 0 && propertyType == null && !rented) {
            propertiesPage = propertyRepository.findAllByRentedAndUser_Id(false,userId,pageable);
        }
        else {
            if(maxPrice == 0.0){
                maxPrice  = Double.MAX_VALUE;
            }


            propertiesPage = propertyRepository.findByCityAndPriceAndSizeAndUserId(city, maxPrice, minSize, propertyType, rented, userId,pageable);
        }

        List<Property> properties = propertiesPage.getContent().stream().map(PropertyConverter::convert).toList();
        return new PageImpl<>(properties, pageable, propertiesPage.getTotalElements());
    }


}
