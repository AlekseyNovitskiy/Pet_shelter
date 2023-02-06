package com.example.pet_shelter.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UsersService usersService;

    @Test
    void testGetAllUsers() throws Exception {
        when(usersService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/getALL");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetAllUsers2() throws Exception {
        when(usersService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/user/getALL");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testCreateUser() throws Exception {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        when(usersService.createUserInDb((Users) any())).thenReturn(users);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = new Users();
        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");
        String content = (new ObjectMapper()).writeValueAsString(users1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":11,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"userPhoneNumber\":\"4105551212\",\"userEmail\":\"jane.doe"
                                        + "@example.org\",\"dog\":{\"id\":11,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}}"));
    }

    @Test
    void testDeleteDog() throws Exception {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        when(usersService.deleteUser((Long) any())).thenReturn(users);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/delete/{id}", 11L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":11,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"userPhoneNumber\":\"4105551212\",\"userEmail\":\"jane.doe"
                                        + "@example.org\",\"dog\":{\"id\":11,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}}"));
    }

    @Test
    void testDeleteDog2() throws Exception {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        when(usersService.updateUser((Long) any(), (Users) any())).thenReturn(users);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = new Users();
        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");
        String content = (new ObjectMapper()).writeValueAsString(users1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/user/update/dog/{id}", 11L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":11,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"userPhoneNumber\":\"4105551212\",\"userEmail\":\"jane.doe"
                                        + "@example.org\",\"dog\":{\"id\":11,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}}"));
    }

    @Test
    void testUpdateUsers() throws Exception {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        Users users = new Users();
        users.setDog(dogs);
        users.setFirstName("Jane");
        users.setId(11L);
        users.setLastName("Doe");
        users.setUserEmail("jane.doe@example.org");
        users.setUserPhoneNumber("4105551212");
        when(usersService.updateUser(any(), (Users) any())).thenReturn(users);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        Users users1 = new Users();
        users1.setDog(dogs1);
        users1.setFirstName("Jane");
        users1.setId(11L);
        users1.setLastName("Doe");
        users1.setUserEmail("jane.doe@example.org");
        users1.setUserPhoneNumber("4105551212");

        String content = (new ObjectMapper()).writeValueAsString(users1);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/update/{id}", 11L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":11,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"userPhoneNumber\":\"4105551212\",\"userEmail\":\"jane.doe"
                                        + "@example.org\",\"dog\":{\"id\":11,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}}"));
    }
}

