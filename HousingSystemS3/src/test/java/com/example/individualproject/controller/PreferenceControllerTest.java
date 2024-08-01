package com.example.individualproject.controller;

import com.example.individualproject.business.PreferenceUseCases;
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
class PreferenceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PreferenceUseCases preferenceUseCases;

    CreatePreferenceRequest CreatePreferenceObject() throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        User user = User.builder()
                .id(1L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith24569@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOME_SEEKER)
                .build();



        return CreatePreferenceRequest.builder()
                .user(user)
                .propertyType(PropertyType.ROOM)
                .city("Breda")
                .price(450.00)
                .room(1)
                .build();
    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOME_SEEKER"})
    void createPreference_shouldReturn201_whenRequestIsValid() throws  Exception {
        CreatePreferenceRequest expectedRequest = CreatePreferenceObject();

        CreatePreferenceResponse response = CreatePreferenceResponse.builder().preferenceId(4L).build();

        when(preferenceUseCases.createPreference(expectedRequest))
                .thenReturn(response);

        mockMvc.perform(post("/preference")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                          {
                              "user":{
                                    "id":1,
                                    "firstName":"Bob",
                                    "lastName":"Smith",
                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                    "email":"bobsmith24569@gmail.com",
                                    "password":"Bobby1!",
                                    "role":"HOME_SEEKER",
                                    "applications" : [],
                                    "properties" : []},
                              "city": "Breda",
                              "propertyType": "ROOM",
                              "price": 450.0,
                              "room": 1
                          }
                                                      
                          """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                               {"preferenceId" : 4}
                                """));

        verify(preferenceUseCases).createPreference(expectedRequest);

    }
    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOME_SEEKER"})
    void createPreference_shouldReturn400_whenRequestIsInvalid() throws  Exception {


        mockMvc.perform(post("/preference")
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
                             "city": "",
                              "propertyType": "",
                              "price": null,
                              "room": null
                          }
                                                      
                          """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Content-Type"));


    }


    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOME_SEEKER"})
    void getPreferenceByUserId_shouldReturn200_WhenUserFound() throws  Exception{
        CreatePreferenceRequest request = CreatePreferenceObject();
        Preference preference = Preference.builder()
                .id(4L)
                .user(request.getUser())
                .propertyType(request.getPropertyType())
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .build();

        when(preferenceUseCases.getPreferenceByUserId(4L))
                .thenReturn(Optional.of(preference));

        mockMvc.perform(get("/preference/id/4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                                {"id":4,
                                                        "user":{
                                                                "id":1,
                                                                "firstName":"Bob",
                                                                "lastName":"Smith",
                                                                "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                                                "email":"bobsmith24569@gmail.com",
                                                                "password":"Bobby1!","role":"HOME_SEEKER"},
                                                        "city":"Breda",
                                                        "propertyType":"ROOM",
                                                        "price":450.0,
                                                        "room":1}
                                                """));
        verify(preferenceUseCases).getPreferenceByUserId(4L);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOME_SEEKER"})
    void getPreferenceByUserId_shouldReturn404_WhenUserNotFound() throws  Exception{

        when(preferenceUseCases.getPreferenceByUserId(4L))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/preference/id/4"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(preferenceUseCases).getPreferenceByUserId(4L);

    }




    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOME_SEEKER"})
    void updatePreference_shouldReturn204() throws Exception{
        mockMvc.perform(put("/preference/4")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                          {
                              "user":{
                                    "id":1,
                                    "firstName":"Bob",
                                    "lastName":"Smith",
                                    "dateOfBirth":"2003-10-11T00:00:00.000+00:00",
                                    "email":"bobsmith24569@gmail.com",
                                    "password":"Bobby1!",
                                    "role":"HOME_SEEKER",
                                    "applications" : [],
                                    "properties" : []},
                              "city": "Breda",
                              "propertyType": "ROOM",
                              "price": 450.0,
                              "room": 1
                          }
                                                      
                          """))
                .andDo(print())
                .andExpect(status().isNoContent());

        CreatePreferenceRequest request = CreatePreferenceObject();
        UpdatePreferenceRequest expectedRequest = UpdatePreferenceRequest.builder()
                .id(4L)
                .user(request.getUser())
                .propertyType(request.getPropertyType())
                .city(request.getCity())
                .price(request.getPrice())
                .room(request.getRoom())
                .build();
        verify(preferenceUseCases).updatePreference(expectedRequest);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOME_SEEKER"})
    void deleteProperty_shouldReturn204() throws  Exception{
        mockMvc.perform(delete("/preference/4"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(preferenceUseCases).deletePreference(4L);
    }

}