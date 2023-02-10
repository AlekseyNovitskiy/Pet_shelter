package com.example.pet_shelter.controllers;

import com.example.pet_shelter.repository.*;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;
import com.example.pet_shelter.service.ReportUsersService;
import com.example.pet_shelter.service.UsersService;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

public class ReportUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogsRepository repositoryDog;

    @MockBean
    private UsersRepository repositoryUsers;

    @MockBean
    private DogsFotoRepository repositoryFoto;

    @MockBean
    private ReportUsersRepository repository;

    @MockBean
    private TelegramBot bot;

    @MockBean
    private BinaryContentFileRepository binaryContentFileRepository;

    @MockBean
    private ReportUsersService service;

    @SpyBean
    private DogsService serviceDog;

    @SpyBean
    private UsersService serviceUsers;

    @SpyBean
    private DogsFotoService fotoServices;

    @InjectMocks
    private ReportUserController controller;


    @Test
    public void downloadReportUserTest() throws Exception {

    }

}
