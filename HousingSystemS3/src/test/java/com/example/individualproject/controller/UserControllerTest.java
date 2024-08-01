package com.example.individualproject.controller;

import com.example.individualproject.business.UserUseCases;
import com.example.individualproject.domain.*;
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
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserUseCases userUseCases;

    @Test
    void createUser_shouldReturn201_whenRequestIsValid() throws  Exception {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CreateUserRequest expectedRequest = CreateUserRequest.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith2456@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();

CreateUserResponse response = CreateUserResponse.builder().userId(410L).build();

        when(userUseCases.createUser(expectedRequest))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "firstName": "Bob",
                                    "lastName": "Smith",
                                    "email": "bobsmith2456@gmail.com",
                                    "password": "Bobby1!",
                                    "dateOfBirth": "2003-10-11",
                                    "role": "HOMEOWNER"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                        {"userId" : 410}
                                         """));
        verify(userUseCases).createUser(expectedRequest);

    }

    @Test
    void createUser_shouldReturn400_whenRequestIsInvalid() throws  Exception {


        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "firstName": "",
                                    "lastName": "",
                                    "email": "",
                                    "password": "",
                                    "dateOfBirth": "",
                                    "role": ""
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("Content-Type"));


    }

    @Test
    @WithMockUser(username = "rafaelbe@gmail.com", roles ={"HOMEOWNER"})
    void getUsers_shouldReturn200_WhenGettingUsersList() throws Exception{
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = User.builder()
                .id(44L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith24567@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();

        when(userUseCases.getUsers())
                .thenReturn(GetUsersResponse.builder().users(List.of(user)).build());

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                            {
                                                "users": [
                                                    {"id":44, "firstName":"Bob", "lastName":"Smith", "email":"bobsmith24567@gmail.com", "password":"Bobby1!", "dateOfBirth":"2003-10-11T00:00:00.000+00:00", "role":"HOMEOWNER" }
                                                ]
                                            }
                                            """));
        verify(userUseCases).getUsers();

    }
    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getUserById_shouldReturn200_WhenUserFound() throws  Exception{
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = User.builder()
                .id(54L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith24569@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();
            when(userUseCases.getUserById(54L))
                    .thenReturn(Optional.of(user));
            mockMvc.perform(get("/users/id/54"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("""
                                                {"id":54, "firstName":"Bob", "lastName":"Smith", "email":"bobsmith24569@gmail.com", "password":"Bobby1!", "dateOfBirth":"2003-10-11T00:00:00.000+00:00", "role":"HOMEOWNER" }
                                                """));
            verify(userUseCases).getUserById(54L);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getUserById_shouldReturn404_WhenUserNotFound() throws  Exception{

        when(userUseCases.getUserById(54L))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/users/id/54"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(userUseCases).getUserById(54L);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getUserByEmail_shouldReturn200_WhenUserFound() throws  Exception{
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = User.builder()
                .id(54L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith24569@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();
        when(userUseCases.getUserByEmail("bobsmith24569@gmail.com"))
                .thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/email/bobsmith24569@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                                                {"id":54, "firstName":"Bob", "lastName":"Smith", "email":"bobsmith24569@gmail.com", "password":"Bobby1!", "dateOfBirth":"2003-10-11T00:00:00.000+00:00", "role":"HOMEOWNER" }
                                                """));
        verify(userUseCases).getUserByEmail("bobsmith24569@gmail.com");

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void getUserByEmail_shouldReturn404_WhenUserNotFound() throws  Exception{

        when(userUseCases.getUserByEmail("bobsmith24569@gmail.com"))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/users/email/bobsmith24569@gmail.com"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(userUseCases).getUserByEmail("bobsmith24569@gmail.com");

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void updateUser_shouldReturn204() throws Exception{
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mockMvc.perform(put("/users/54")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "firstName": "Bob",
                                    "lastName": "Smith",
                                    "dateOfBirth": "2003-10-11T00:00:00.000+00:00",
                                    "email": "bobsmith24569@gmail.com",
                                    "password": "Bobby1!",
                                    "role": "HOMEOWNER"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isNoContent());

        UpdateUserRequest expectedRequest = UpdateUserRequest.builder()
                .id(54L)
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith24569@gmail.com")
                .password("Bobby1!")
                .dateOfBirth(dateTimeFormat.parse("2003-10-11 02:00:00"))
                .role(Role.HOMEOWNER)
                .build();
        verify(userUseCases).updateUser(expectedRequest);

    }

    @Test
    @WithMockUser(username = "bobsmith24569@gmail.com", roles ={"HOMEOWNER"})
    void deleteUser_shouldReturn204() throws  Exception{
        mockMvc.perform(delete("/users/54"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(userUseCases).deleteUser(54L);
    }


}