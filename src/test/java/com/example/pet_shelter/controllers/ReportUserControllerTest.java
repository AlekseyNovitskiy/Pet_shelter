package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Shelters;
import com.example.pet_shelter.repository.*;
import com.example.pet_shelter.service.*;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ReportUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogsRepository repositoryDog;

    @MockBean
    private ShelterRepository repositoryShelter;

    @MockBean
    private UsersRepository repositoryUsers;

    @MockBean
    private DogsFotoRepository repositoryFoto;

    @MockBean
    private ReportUsersRepository repository;

    @MockBean
    private CatsRepository repositoryCat;

    @MockBean
    private CatsFotoRepository repositoryCatsFoto;

    @MockBean
    private TelegramBot bot;

    @MockBean
    private BinaryContentFileRepository binaryContentFileRepository;

    @MockBean
    private CatsService serviceCats;

    @MockBean
    private ReportUsersService service;

    @SpyBean
    private DogsService serviceDog;

    @SpyBean
    private ShelterService serviceShelter;

    @SpyBean
    private UsersService serviceUsers;

    @SpyBean
    private DogsFotoService fotoServices;

    @SpyBean
    private CatsFotoService fotoCarsServices;

    @InjectMocks
    private ReportUserController controller;


    @Test
    public void downloadReportUserTest() throws Exception {

    }

}
