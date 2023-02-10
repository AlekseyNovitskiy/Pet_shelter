package com.example.pet_shelter.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.repository.DogsFotoRepository;
import com.example.pet_shelter.repository.DogsRepository;

import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DogsFotoService.class})
@ExtendWith(SpringExtension.class)
class DogsFotoServiceTest {
    @MockBean
    private DogsFotoRepository dogsFotoRepository;

    @Autowired
    private DogsFotoService dogsFotoService;

    @MockBean
    private DogsRepository dogsRepository;


    @Test
    void testFindDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Bella");
        when(dogsRepository.findById((Long) any())).thenReturn(Optional.of(dogs));
        assertSame(dogs, dogsFotoService.findDog(11L));
        verify(dogsRepository).findById((Long) any());
    }

    @Test
    void testFindFotoDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Bella");

        DogsFoto dogsFoto = new DogsFoto();
        dogsFoto.setDog(dogs);
        dogsFoto.setFilePath("/directory/foo.txt");
        dogsFoto.setFileSize(3L);
        dogsFoto.setId(11L);
        dogsFoto.setMediaType("Media Type");
        Optional<DogsFoto> ofResult = Optional.of(dogsFoto);
        when(dogsFotoRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(dogsFoto, dogsFotoService.findFotoDog(11L));
        verify(dogsFotoRepository).findById((Long) any());
    }
}

