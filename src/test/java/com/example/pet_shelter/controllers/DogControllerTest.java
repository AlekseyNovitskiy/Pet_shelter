package com.example.pet_shelter.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.DogsFotoRepository;
import com.example.pet_shelter.repository.DogsRepository;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

class DogControllerTest {

    @Test
    void testGetAllDogs() {
        DogsRepository dogsRepository = mock(DogsRepository.class);
        ArrayList<Dogs> dogsList = new ArrayList<>();
        when(dogsRepository.findAll()).thenReturn(dogsList);
        DogsService dogsService = new DogsService(dogsRepository);
        Collection<Dogs> actualAllDogs = (new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)))).getAllDogs();
        assertSame(dogsList, actualAllDogs);
        assertTrue(actualAllDogs.isEmpty());
        verify(dogsRepository).findAll();
    }

    @Test
    void testGetAllDogs3() {

        DogsService dogsService = mock(DogsService.class);
        ArrayList<Dogs> dogsList = new ArrayList<>();
        when(dogsService.getAllDogs()).thenReturn(dogsList);
        Collection<Dogs> actualAllDogs = (new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)))).getAllDogs();
        assertSame(dogsList, actualAllDogs);
        assertTrue(actualAllDogs.isEmpty());
        verify(dogsService).getAllDogs();
    }


    @Test
    void testCreateDog() {

        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        DogsRepository dogsRepository = mock(DogsRepository.class);
        when(dogsRepository.save((Dogs) any())).thenReturn(dogs);
        DogsService dogsService = new DogsService(dogsRepository);
        DogController dogController = new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)));

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        assertSame(dogs, dogController.createDog(dogs1));
        verify(dogsRepository).save((Dogs) any());
    }

    @Test
    void testCreateDog2() {

        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        DogsService dogsService = mock(DogsService.class);
        when(dogsService.createDogInDB((Dogs) any())).thenReturn(dogs);
        DogController dogController = new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class),
                        mock(DogsFotoRepository.class)));

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        assertSame(dogs, dogController.createDog(dogs1));
        verify(dogsService).createDogInDB((Dogs) any());
    }


    @Test
    void testDeleteDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");

        DogsRepository dogsRepository = mock(DogsRepository.class);
        when(dogsRepository.findById((Long) any())).thenReturn(Optional.of(dogs));
        doNothing().when(dogsRepository).deleteById((Long) any());
        DogsService dogsService = new DogsService(dogsRepository);
        assertSame(dogs, (
                new DogController(dogsService,
                        new DogsFotoService(mock(DogsRepository.class),
                                mock(DogsFotoRepository.class)))).
                deleteDog(11L));

        verify(dogsRepository).findById((Long) any());
        verify(dogsRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteDog2() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        DogsService dogsService = mock(DogsService.class);
        when(dogsService.deleteDog((Long) any())).thenReturn(dogs);
        assertSame(dogs, (new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)))).deleteDog(11L));
        verify(dogsService).deleteDog((Long) any());
    }

    @Test
    void testUpdateDog() {
        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        Optional<Dogs> ofResult = Optional.of(dogs);

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");
        DogsRepository dogsRepository = mock(DogsRepository.class);
        when(dogsRepository.save((Dogs) any())).thenReturn(dogs1);
        when(dogsRepository.findById((Long) any())).thenReturn(ofResult);
        DogsService dogsService = new DogsService(dogsRepository);
        DogController dogController = new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)));

        Dogs dogs2 = new Dogs();
        dogs2.setAge(1);
        dogs2.setId(11L);
        dogs2.setInfoDog("Info Dog");
        dogs2.setNickname("Sharik");
        assertSame(dogs1, dogController.updateDog(11L, dogs2));
        verify(dogsRepository).save((Dogs) any());
        verify(dogsRepository).findById((Long) any());
    }

    @Test
    void testUpdateDog2() {

        Dogs dogs = new Dogs();
        dogs.setAge(1);
        dogs.setId(11L);
        dogs.setInfoDog("Info Dog");
        dogs.setNickname("Sharik");
        DogsService dogsService = mock(DogsService.class);
        when(dogsService.updateDog((Long) any(), (Dogs) any())).thenReturn(dogs);

        DogController dogController = new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)));

        Dogs dogs1 = new Dogs();
        dogs1.setAge(1);
        dogs1.setId(11L);
        dogs1.setInfoDog("Info Dog");
        dogs1.setNickname("Sharik");

        assertSame(dogs, dogController.updateDog(11L, dogs1));
        verify(dogsService).updateDog((Long) any(), (Dogs) any());
    }

    @Test
    void testUploadFotoDog() throws IOException {

        DogsService dogsService = new DogsService(mock(DogsRepository.class));
        DogController dogController = new DogController(dogsService,
                new DogsFotoService(mock(DogsRepository.class), mock(DogsFotoRepository.class)));
        ResponseEntity<String> actualUploadFotoDogResult = dogController.uploadFotoDog(11L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAA".getBytes("UTF-8"))));
        assertEquals("Файл большого размера", actualUploadFotoDogResult.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualUploadFotoDogResult.getStatusCode());
        assertTrue(actualUploadFotoDogResult.getHeaders().isEmpty());
    }
}

