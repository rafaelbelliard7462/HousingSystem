package com.example.individualproject.controller;

import com.example.individualproject.business.ApplicationUseCases;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
import com.example.individualproject.domain.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationUseCases applicationUseCases;

    CreateApplicationRequest CreateApplicationObject() throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        User user = User.builder()
                .id(23L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith245691@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();

        User user1 = User.builder()
                .id(13L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith1@gmail.com")  // Corrected email address
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOME_SEEKER)
                .build();

        Address address = Address.builder()
                .street("Molenslag 621")
                .city("Breda")
                .postCode("4817GZ")
                .build();

        Property property = Property.builder()
                .id(23L)
                .user(user)
                .propertyType(PropertyType.ROOM)
                .address(address)
                .price(450.00)
                .description("Some description")
                .available(dateTimeFormat.parse("2023-10-11 02:00:00"))
                .room(1)
                .rented(false)
                .rentedDate(null)
                .build();

        return CreateApplicationRequest.builder()
                .user(user1)
                .property(property)
                .appliedDate(dateTimeFormat.parse("2023-11-11 01:00:00"))
                .status(Status.PENDING)
                .description("Some description")
                .build();
    }


    @Test
    @WithMockUser(username = "bobsmith1@gmail.com", roles ={"HOME_SEEKER"})
    void createApplication_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateApplicationRequest expectedRequest = CreateApplicationObject();

        CreateApplicationResponse response = CreateApplicationResponse.builder().applicationId(47L).build();

        when(applicationUseCases.createApplication(expectedRequest))
                .thenReturn(response);

        mockMvc.perform(post("/applications")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
             {
               "user": {
                "id": 13,
                "firstName": "Bob",
                "lastName": "Smith",
                "email": "bobsmith1@gmail.com",
                "password": "Bobby1!",
                "dateOfBirth": "2003-10-11",
                "role": "HOME_SEEKER",
                "applications": [],
                "properties": []
                
               },
               "property": {
                "id": 23,
                "user": {
                  "id": 23,
                  "firstName": "Bob",
                  "lastName": "Smith",
                  "email": "bobsmith245691@gmail.com",
                  "password": "Bobby1!",
                  "dateOfBirth": "2003-10-11",
                  "role": "HOMEOWNER",
                  "applications": [],
                  "properties": []
                },
                "address": {
                  "street": "Molenslag 621",
                  "city": "Breda",
                  "postCode": "4817GZ"
                },
                "propertyType": "ROOM",
                "price": 450.0,
                "room": 1,
                "available": "2023-10-11",
                "description": "Some description",
                "rented": false,
                "rentedDate": null,
                "applications" : [],
                "propertyImages" : []
               },
               "appliedDate": "2023-11-11",
               "status": "PENDING",
               "description": "Some description"
             }
      
           """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                             {"applicationId" : 47}
                              """));

        verify(applicationUseCases).createApplication(expectedRequest);
    }

    @Test
    @WithMockUser(username = "bobsmith245691@gmail.com", roles ={"HOME_SEEKER"})
    void createApplication_shouldReturn400_whenRequestIsInvalid() throws  Exception {


        mockMvc.perform(post("/applications")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                          "user": {
                "id": null,
                "firstName": "",
                "lastName": "",
                "email": "",
                "password": "",
                "dateOfBirth": "",
                "role": ""
               },
               "property": {
                "id": null,
                "user": {
                  "id": null,
                  "firstName": "",
                  "lastName": "",
                  "email": "",
                  "password": "",
                  "dateOfBirth": "",
                  "role": ""
                },
                "address": {
                  "street": "",
                  "city": "",
                  "postCode": ""
                },
                "propertyType": "",
                "price": null,
                "room": null,
                "available": null,
                "description": "",
                "rented": null,
                "rentedDate": null
               },
               "appliedDate": null,
               "status": "",
               "description": ""
             }

                          """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Content-Type"));


    }

    @Test
    @WithMockUser(username = "bobsmith245691@gmail.com", roles ={"HOMEOWNER"})
    void getApplicationsByProperty_shouldReturn200_WhenGettingApplicationListByPropertyId() throws Exception{
        CreateApplicationRequest request = CreateApplicationObject();
        Application application = Application.builder()
                .id(13L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationUseCases.getApplicationsByProperty(23L))
                .thenReturn(GetApplicationsResponse.builder().applications(List.of(application)).build());

        mockMvc.perform(get("/applications/findApplicationsByProperty/23"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                            {"applications":[
                                            {"id":13,
                                            "user":{
                                                    "id":13,
                                                    "firstName":"Bob",
                                                    "lastName":"Smith",
                                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                    "email":"bobsmith1@gmail.com",
                                                    "password":"Bobby1!",
                                                    "role":"HOME_SEEKER"},
                                            "property":{
                                                    "id":23,
                                                    "user":{
                                                            "id":23,
                                                            "firstName":"Bob",
                                                            "lastName":"Smith",
                                                            "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                            "email":"bobsmith245691@gmail.com",
                                                            "password":"Bobby1!",
                                                            "role":"HOMEOWNER"},
                                                    "address":{
                                                            "id":null,
                                                            "street":"Molenslag 621",
                                                            "city":"Breda",
                                                            "postCode":"4817GZ"},
                                                    "propertyType":"ROOM",
                                                    "price":450.0,
                                                    "room":1,
                                                    "available":"2023-10-11T00:00:00.000+00:00",
                                                    "description":"Some description",
                                                    "rented":false,
                                                    "rentedDate":null},
                                            "appliedDate":"2023-11-11T00:00:00.000+00:00",
                                            "status":"PENDING",
                                            "description":"Some description"}]}
                                            """));
        verify(applicationUseCases).getApplicationsByProperty(23L);

    }

    @Test
    @WithMockUser(username = "bobsmith1@gmail.com", roles ={"HOME_SEEKER"})
    void getApplicationsByUser_shouldReturn200_WhenGettingApplicationListByUserId() throws Exception{
        CreateApplicationRequest request = CreateApplicationObject();
        Application application = Application.builder()
                .id(13L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationUseCases.getApplicationsByUser(13L))
                .thenReturn(GetApplicationsResponse.builder().applications(List.of(application)).build());

        mockMvc.perform(get("/applications/findApplicationsByUser/13"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                            {"applications":[
                                            {"id":13,
                                            "user":{
                                                    "id":13,
                                                    "firstName":"Bob",
                                                    "lastName":"Smith",
                                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                    "email":"bobsmith1@gmail.com",
                                                    "password":"Bobby1!",
                                                    "role":"HOME_SEEKER"},
                                            "property":{
                                                    "id":23,
                                                    "user":{
                                                            "id":23,
                                                            "firstName":"Bob",
                                                            "lastName":"Smith",
                                                            "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                            "email":"bobsmith245691@gmail.com",
                                                            "password":"Bobby1!",
                                                            "role":"HOMEOWNER"},
                                                    "address":{
                                                            "id":null,
                                                            "street":"Molenslag 621",
                                                            "city":"Breda",
                                                            "postCode":"4817GZ"},
                                                    "propertyType":"ROOM",
                                                    "price":450.0,
                                                    "room":1,
                                                    "available":"2023-10-11T00:00:00.000+00:00",
                                                    "description":"Some description",
                                                    "rented":false,
                                                    "rentedDate":null},
                                            "appliedDate":"2023-11-11T00:00:00.000+00:00",
                                            "status":"PENDING",
                                            "description":"Some description"}]}
                                            """));
        verify(applicationUseCases).getApplicationsByUser(13L);

    }

    @Test
    @WithMockUser(username = "bobsmith245691@gmail.com", roles ={"HOMEOWNER"})
    void getApplicationById_shouldReturn200_WhenUserFound() throws  Exception{
        CreateApplicationRequest request = CreateApplicationObject();
        Application application = Application.builder()
                .id(13L)
                .user(request.getUser())
                .property(request.getProperty())
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();

        when(applicationUseCases.getApplicationById(13L))
                .thenReturn(Optional.of(application));

        mockMvc.perform(get("/applications/id/13"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                               {"id":13,
                                            "user":{
                                                    "id":13,
                                                    "firstName":"Bob",
                                                    "lastName":"Smith",
                                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                    "email":"bobsmith1@gmail.com",
                                                    "password":"Bobby1!",
                                                    "role":"HOME_SEEKER"},
                                            "property":{
                                                    "id":23,
                                                    "user":{
                                                            "id":23,
                                                            "firstName":"Bob",
                                                            "lastName":"Smith",
                                                            "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                            "email":"bobsmith245691@gmail.com",
                                                            "password":"Bobby1!",
                                                            "role":"HOMEOWNER"},
                                                    "address":{
                                                            "id":null,
                                                            "street":"Molenslag 621",
                                                            "city":"Breda",
                                                            "postCode":"4817GZ"},
                                                    "propertyType":"ROOM",
                                                    "price":450.0,
                                                    "room":1,
                                                    "available":"2023-10-11T00:00:00.000+00:00",
                                                    "description":"Some description",
                                                    "rented":false,
                                                    "rentedDate":null},
                                            "appliedDate":"2023-11-11T00:00:00.000+00:00",
                                            "status":"PENDING",
                                            "description":"Some description"}
                                                """));
        verify(applicationUseCases).getApplicationById(13L);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getApplicationById_shouldReturn404_WhenUserNotFound() throws  Exception{

        when(applicationUseCases.getApplicationById(48L))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/applications/id/48"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(applicationUseCases).getApplicationById(48L);

    }




    @Test
    @WithMockUser(username = "bobsmith245691@gmail.com", roles ={"HOMEOWNER"})
    void updateApplication_shouldReturn204() throws Exception{
        mockMvc.perform(put("/applications/13")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
             {
               "user": {
                "id": 13,
                "firstName": "Bob",
                "lastName": "Smith",
                "email": "bobsmith1@gmail.com",
                "password": "Bobby1!",
                "dateOfBirth": "2003-10-11",
                "role": "HOME_SEEKER",
                "applications": [],
                "properties": []
                
               },
               "property": {
                "id": 23,
                "user": {
                  "id": 23,
                  "firstName": "Bob",
                  "lastName": "Smith",
                  "email": "bobsmith245691@gmail.com",
                  "password": "Bobby1!",
                  "dateOfBirth": "2003-10-11",
                  "role": "HOMEOWNER",
                  "applications": [],
                  "properties": []
                },
                "address": {
                  "street": "Molenslag 621",
                  "city": "Breda",
                  "postCode": "4817GZ"
                },
                "propertyType": "ROOM",
                "price": 450.0,
                "room": 1,
                "available": "2023-10-11",
                "description": "Some description",
                "rented": false,
                "rentedDate": null,
                "applications" : [],
                "propertyImages" : []
               },
               "appliedDate": "2023-11-11",
               "status": "PENDING",
               "description": "Some description"
             }
      
           """))
                .andDo(print())
                .andExpect(status().isNoContent());

        CreateApplicationRequest request = CreateApplicationObject();
        UpdateApplicationRequest expectedRequest = UpdateApplicationRequest.builder()
                .id(13L)
                .appliedDate(request.getAppliedDate())
                .status(request.getStatus())
                .description(request.getDescription())
                .build();
        verify(applicationUseCases).updateApplication(expectedRequest);

    }

    @Test
    @WithMockUser(username = "bobsmith245691@gmail.com", roles ={"HOMEOWNER"})
    void deleteApplication_shouldReturn204() throws  Exception{
        mockMvc.perform(delete("/applications/13"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(applicationUseCases).deleteApplication(13L);
    }


}