package com.example.pet_shelter.controllers;


import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.*;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;
import com.example.pet_shelter.service.ReportUsersService;
import com.example.pet_shelter.service.UsersService;
import com.pengrad.telegrambot.TelegramBot;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@WebMvcTest
public class DogControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogsRepository repository;

    @MockBean
    private UsersRepository repositoryUsers;

    @MockBean
    private DogsFotoRepository repositoryFoto;

    @MockBean
    private ReportUsersRepository reportUsersRepository;

    @MockBean
    private TelegramBot bot;

    @MockBean
    private BinaryContentFileRepository binaryContentFileRepository;

    @SpyBean
    private DogsService service;

    @SpyBean
    private UsersService serviceUsers;

    @SpyBean
    private DogsFotoService fotoServices;

    @SpyBean
    private ReportUsersService reportUsersService;

    @InjectMocks
    private DogController controller;

    @Test
    public void testCreateDog() throws Exception {
        String nickname = "Sharik";
        Long id = 11L;
        int age = 1;
        String info = "Info Dog";

        JSONObject dogObject = new JSONObject();
        dogObject.put("id", id);
        dogObject.put("nickname", nickname);
        dogObject.put("age", age);
        dogObject.put("infoDog", info);

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);


        when(repository.save(any(Dogs.class))).thenReturn(dog);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(dog));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/dogs/create")
                        .content(dogObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.infoDog").value(info));

    }

    @Test
    public void testCreateDog2() {
        String nickname = "";
        Long id = 11L;
        int age = 1;
        String info = "Info Dog";

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);

        when(repository.save(any(Dogs.class))).thenReturn(dog);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(dog));

        assertThrows(DogNullParameterValueException.class, () -> service.createDogInDB(dog));
    }


    @Test
    public void testDeleteDog() throws Exception {
        String nickname = "Sharik";
        Long id = 11L;
        int age = 1;
        String info = "Info Dog";

        JSONObject dogObject = new JSONObject();
        dogObject.put("id", id);
        dogObject.put("nickname", nickname);
        dogObject.put("age", age);
        dogObject.put("infoDog", info);

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);

        when(repository.save(any(Dogs.class))).thenReturn(dog);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(dog));
        doNothing().when(repository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/dogs/delete/" + 5L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(
                        "{\"id\":11,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}"));
    }


    @Test
    public void testUpdateDog() throws Exception {
        String nickname = "Sharik";
        Long id = 11L;
        int age = -1;
        String info = "Info Dog";

        JSONObject dogObject = new JSONObject();
        dogObject.put("id", id);
        dogObject.put("nickname", nickname);
        dogObject.put("age", age);
        dogObject.put("infoDog", info);

        Dogs dog = new Dogs();
        dog.setId(id);
        dog.setNickname(nickname);
        dog.setAge(age);
        dog.setInfoDog(info);

        when(repository.save(any(Dogs.class))).thenReturn(dog);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(dog));

        Dogs dog2 = new Dogs();
        dog.setId(1L);
        dog.setNickname("Sharik");
        dog.setAge(5);
        dog.setInfoDog("Info Dog");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/dogs/update/" + id, dog)
                        .content(dogObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string
                        ("{\"id\":1,\"nickname\":\"Sharik\",\"age\":1,\"infoDog\":\"Info Dog\"}"));

    }

    @Test
    public void testUploadFotoDog() throws Exception {
        ResponseEntity<String> actualUploadFotoDogResult = controller.uploadFotoDog(11L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAA".getBytes("UTF-8"))));

        assertEquals("Файл большого размера", actualUploadFotoDogResult.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualUploadFotoDogResult.getStatusCode());
        assertTrue(actualUploadFotoDogResult.getHeaders().isEmpty());
    }
}