package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/dogs")
public class DogController {

    @Value ("${size.FotoDogs}")
    int SIZE_FOTO_DOG; // Максимальный размер файла

    @Autowired
    private DogsService dogsService;

    @Autowired
    private DogsFotoService dogsFotoService;

    @PostMapping("/create")   // Создание новой записи о собаке
    public Dogs createDog (@RequestBody Dogs dogs) {
        return this.dogsService.createDog(dogs);
    }

    @PostMapping(value = "/{id}/fotoDog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  // Прикрепление фото собаки
    public  ResponseEntity<String> uploadFotoDog(@PathVariable Long id, @RequestParam MultipartFile foto) throws IOException {
        if (foto.getSize() >= SIZE_FOTO_DOG) {         // Ограничение размера файла
            return ResponseEntity.badRequest().body("Файл большого размера");
        }
        dogsFotoService.uploadFotoDog(id,foto);
        return ResponseEntity.ok().build();
    }


  //  @GetMapping(value = "/{id}/fotoDog") // Получение фото собаки по id
   // public ResponseEntity<byte[]> downloadFotoDog(@PathVariable Long id) {

  //  }



}
