package com.example.individualproject.controller;

import com.example.individualproject.business.PropertyUseCases;
import com.example.individualproject.domain.*;
import com.example.individualproject.domain.enums.PropertyType;
import com.example.individualproject.domain.enums.Role;
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
import java.util.Date;
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
class PropertyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PropertyUseCases propertyUseCases;

    CreatePropertyRequest CreatePropertyObject() throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        User user = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith24569@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();

        Address address = Address.builder()
                .street("Molenslag 62")
                .city("Breda")
                .postCode("4817GZ")
                .build();

        return CreatePropertyRequest.builder()
                .userId(user.getId())
                .propertyType(PropertyType.ROOM)
                .address(address)
                .price(450.00)
                .description("Some description")
                .available(dateTimeFormat.parse("2023-10-11 02:00:00"))
                .room(1)
                .rented(false)
                .rentedDate(null)
                .build();
    }
    User createUser() throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@gmil.com")
                .password("Bobby!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();
    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void createProperty_shouldReturn201_whenRequestIsValid() throws  Exception {
        CreatePropertyRequest expectedRequest = CreatePropertyObject();

        CreatePropertyResponse response = CreatePropertyResponse.builder().propertyId(4L).build();

        when(propertyUseCases.createProperty(expectedRequest))
                .thenReturn(response);

        mockMvc.perform(post("/properties")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                          {
                              "userId": 1,
                              "address": {
                                  "street": "Molenslag 62",
                                  "city": "Breda",
                                  "postCode": "4817GZ"
                              },
                              "propertyType": "ROOM",
                              "price": 450.0,
                              "room": 1,
                              "available": "2023-10-11",
                              "description": "Some description",
                              "rented": false,
                              "rentedDate": null
                          }
                                                      
                          """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                               {"propertyId" : 4}
                                """));

        verify(propertyUseCases).createProperty(expectedRequest);

    }
    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void createProperty_shouldReturn400_whenRequestIsInvalid() throws  Exception {


        mockMvc.perform(post("/properties")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                          {
                              "user": {
                                  "id" : null,
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
                              "available": "",
                              "description": "",
                              "rented": null ,
                              "rentedDate": null
                          }
                                                      
                          """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Content-Type"));


    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getProperties_shouldReturn200_WhenGettingPropertiesList() throws Exception{
        CreatePropertyRequest request = CreatePropertyObject();
        Property property = Property.builder()
                .id(4L)
                .user(createUser())
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .build();

        when(propertyUseCases.getProperties())
                .thenReturn(GetPropertiesResponse.builder().properties(List.of(property)).build());

        mockMvc.perform(get("/properties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                            {"properties":[{"id":4,
                                                            "user":{"id":1,
                                                                    "firstName":"Bob",
                                                                    "lastName":"Smith",
                                                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                                    "email":"bobsmith@gmil.com",
                                                                    "password":"Bobby!",
                                                                    "role":"HOMEOWNER",
                                                                    "properties":[],
                                                                    "applications":[]},
                                                                    "address":{"id":null,
                                                                               "street":"Molenslag 62",
                                                                               "city":"Breda",
                                                                               "postCode":"4817GZ"},
                                                                   "propertyType":"ROOM",
                                                                   "price":450.0,
                                                                   "room":1,
                                                                   "available":"2023-10-11T00:00:00.000+00:00",
                                                                   "description":"Some description",
                                                                   "rented":false,
                                                                   "rentedDate":null,
                                                                   "applications":[]}]}
                                            """));
        verify(propertyUseCases).getProperties();

    }
    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getPropertiesByUser_shouldReturn200_WhenGettingPropertiesListByUser() throws Exception{
        CreatePropertyRequest request = CreatePropertyObject();
        Property property = Property.builder()
                .id(4L)
                .user(createUser())
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .build();

        when(propertyUseCases.getPropertiesByUser(1L))
                .thenReturn(GetPropertiesResponse.builder().properties(List.of(property)).build());

        mockMvc.perform(get("/properties/findPropertiesByUser/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                            {"properties":[{"id":4,
                                                            "user":{"id":1,
                                                                    "firstName":"Bob",
                                                                    "lastName":"Smith",
                                                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                                    "email":"bobsmith@gmil.com",
                                                                    "password":"Bobby!",
                                                                    "role":"HOMEOWNER",
                                                                    "properties":[],
                                                                    "applications":[]},
                                                                    "address":{"id":null,
                                                                               "street":"Molenslag 62",
                                                                               "city":"Breda",
                                                                               "postCode":"4817GZ"},
                                                                    "propertyType":"ROOM",
                                                                    "price":450.0,
                                                                    "room":1,
                                                                    "available":"2023-10-11T00:00:00.000+00:00",
                                                                    "description":"Some description",
                                                                    "rented":false,
                                                                    "rentedDate":null,
                                                                    "applications":[]}]}
                                            
                                            """));
        verify(propertyUseCases).getPropertiesByUser(1L);

    }
    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getPropertyById_shouldReturn200_WhenUserFound() throws  Exception{
        CreatePropertyRequest request = CreatePropertyObject();
        Property property = Property.builder()
                .id(4L)
                .user(createUser())
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .build();

        when(propertyUseCases.getPropertyById(4L))
                .thenReturn(Optional.of(property));

        mockMvc.perform(get("/properties/id/4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                                {"id":4,
                                                "user":{"id":1,
                                                        "firstName":"Bob",
                                                        "lastName":"Smith",
                                                        "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                        "email":"bobsmith@gmil.com",
                                                        "password":"Bobby!",
                                                        "role":"HOMEOWNER",
                                                        "properties":[],
                                                        "applications":[]},
                                                "address":{"id":null,
                                                           "street":"Molenslag 62",
                                                           "city":"Breda",
                                                           "postCode":"4817GZ"},
                                                "propertyType":"ROOM",
                                                "price":450.0,
                                                "room":1,
                                                "available":"2023-10-11T00:00:00.000+00:00",
                                                "description":"Some description",
                                                "rented":false,
                                                "rentedDate":null,
                                                "applications":[]}
                                                """));
        verify(propertyUseCases).getPropertyById(4L);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getPropertyById_shouldReturn404_WhenUserNotFound() throws  Exception{

        when(propertyUseCases.getPropertyById(4L))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/properties/id/4"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(propertyUseCases).getPropertyById(4L);

    }




    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void updateProperty_shouldReturn204() throws Exception{
        mockMvc.perform(put("/properties/4")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                 "userId":1,
                                "address":{"id":null,
                                           "street":"Molenslag 62",
                                           "city":"Breda",
                                           "postCode":"4817GZ"},
                                "propertyType":"ROOM",
                                "price":450.0,
                                "room":1,
                                "available":"2023-10-11T00:00:00.000+00:00",
                                "description":"Some description",
                                "rented":false,
                                "rentedDate":null}}
                                """))
                .andDo(print())
                .andExpect(status().isNoContent());

        CreatePropertyRequest request = CreatePropertyObject();
        UpdatePropertyRequest expectedRequest = UpdatePropertyRequest.builder()
                .id(4L)
                .userId(request.getUserId())
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .price(request.getPrice())
                .description(request.getDescription())
                .available( request.getAvailable())
                .room(request.getRoom())
                .build();
        verify(propertyUseCases).updateProperty(expectedRequest);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void deleteProperty_shouldReturn204() throws  Exception{
        mockMvc.perform(delete("/properties/4"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(propertyUseCases).deleteProperty(4L);
    }


}