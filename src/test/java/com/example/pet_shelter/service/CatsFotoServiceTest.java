package com.example.pet_shelter.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pet_shelter.model.Cats;
import com.example.pet_shelter.model.CatsFoto;
import com.example.pet_shelter.repository.CatsFotoRepository;
import com.example.pet_shelter.repository.CatsRepository;

import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CatsFotoService.class})
@ExtendWith(SpringExtension.class)
class CatsFotoServiceTest {
    @MockBean
    private CatsFotoRepository catsFotoRepository;

    @Autowired
    private CatsFotoService catsFotoService;

    @MockBean
    private CatsRepository catsRepository;


    @Test
    void testFindCat() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        when(catsRepository.findById((Long) any())).thenReturn(Optional.of(cats));
        assertSame(cats, catsFotoService.findCat(11L));
        verify(catsRepository).findById((Long) any());
    }

    @Test
    void testFindFotoCat() {
        Cats cats = new Cats();
        cats.setAge(1);
        cats.setId(11L);
        cats.setInfoCat("Info Cat");
        cats.setNickname("Murzik");

        CatsFoto catsFoto = new CatsFoto();
        catsFoto.setCat(cats);
        catsFoto.setFilePath("/directory/foo.txt");
        catsFoto.setFileSize(3L);
        catsFoto.setId(11L);
        catsFoto.setMediaType("Media Type");

        when(catsFotoRepository.findById((Long) any())).thenReturn(Optional.of(catsFoto));
        assertSame(catsFoto, catsFotoService.findFotoCat(11L));
        verify(catsFotoRepository).findById((Long) any());
    }
}