package com.example.pet_shelter.service;

import com.example.pet_shelter.repository.ReportUsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ReportUsersService.class})
@ExtendWith(SpringExtension.class)
public class ReportUserServiceTest {

    @MockBean
    private ReportUsersRepository repository;

    @Autowired
    private ReportUsersService service;

//    @Test
}
