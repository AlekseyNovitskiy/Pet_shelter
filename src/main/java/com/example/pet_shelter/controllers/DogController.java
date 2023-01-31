package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/dogs")
public class DogController {

    @Value("${size.FotoDogs}")
    int SIZE_FOTO_DOG; // Максимальный размер файла

    private final DogsService dogsService;
    private final DogsFotoService dogsFotoService;

    public DogController(DogsService dogsService, DogsFotoService dogsFotoService) {
        this.dogsService = dogsService;
        this.dogsFotoService = dogsFotoService;
    }

    // Внесение данных о новой собаке
    @PostMapping("/create")   // Создание новой записи о собаке
    @Operation(summary = "Внесение данных о новой питомце")
    public Dogs createDog(@RequestBody Dogs dog) {
        return this.dogsService.createDog(dog);
    }

    // Удаление информации о собаке
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление данных о питомце")
    public Dogs deleteDog(@PathVariable("id") Long id) {
        return this.dogsService.deleteDog(id);
    }

    // Изменение информации о собаке
    @PutMapping("/update/{id}")
    @Operation(summary = "Изменение данных о питомце")
    public Dogs deleteDog(@PathVariable("id") Long id, @RequestBody Dogs dog) {
        return this.dogsService.updateDog(id, dog);
    }

    // Загрузка фотографии собаки
    @PostMapping(value = "/{id}/load/fotoDog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка фотографии питомца")
    // Прикрепление фото собаки
    public ResponseEntity<String> uploadFotoDog(@PathVariable Long id, @RequestParam MultipartFile fotoDog) throws IOException {
        if (fotoDog.getSize() >= SIZE_FOTO_DOG) {         // Ограничение размера файла
            return ResponseEntity.badRequest().body("Файл большого размера");
        }
        dogsFotoService.uploadFotoDog(id, fotoDog);
        return ResponseEntity.ok().build();
    }

    // Просмотр фотографии собаки
    @GetMapping(value = "/{id}/fotoDog")
    @Operation(summary = "Просмотр фотографии питомца")
    public void downloadFotoDog(@PathVariable Long id, HttpServletResponse response) throws IOException {
        DogsFoto dogsFoto = dogsFotoService.findFotoDog(id);
        Path path = Path.of(dogsFoto.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(dogsFoto.getMediaType());
            response.setContentLength((int) dogsFoto.getFileSize());
            is.transferTo(os);
        }
    }

}
