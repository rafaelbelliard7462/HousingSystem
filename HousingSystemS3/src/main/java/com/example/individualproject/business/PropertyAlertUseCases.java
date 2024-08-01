package com.example.individualproject.business;

import com.example.individualproject.persistance.entity.PropertyEntity;

public interface PropertyAlertUseCases {
    void  sendPropertyAlertToUser(PropertyEntity property);
}
