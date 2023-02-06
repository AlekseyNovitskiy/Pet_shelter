package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.service.DogsFotoService;
import com.example.pet_shelter.service.DogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.Collection;

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

    @Operation(
            summary = "Получение списка всех питомцев питомцев",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список питомцев",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dogs.class)))
                    )
            })
    @GetMapping("/getALL")
    public Collection<Dogs> getAllDogs() {
        return this.dogsService.getAllDogs();
    }

    @Operation(
            summary = "Внесение данных о новом питомце",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вернёт объект питомца с данными, которые внесены в базу",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dogs.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка со стороны сервиса(Так же при неполных данных питомца)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка при внесении питомца в базу из-за неверного формата данных"
                    )}
    )
    @PostMapping("/create")
    public Dogs createDog(@Parameter(
            description = "Полные данные питомца",
            example = " {nick_name: 'Name', age : 5, info_dog : 'Описание привычек собаки'")
                          @RequestBody Dogs dog) {
        return this.dogsService.createDogInDB(dog);
    }

    @Operation(
            summary = "Удаление данных о питомце",
            description = "Удаляет питомца по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вернёт объект питомца по указанному id, предварительно удалив его из базы",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dogs.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Питомец не найден в базе"
                    )}
    )
    @DeleteMapping("/delete/{id}")
    public Dogs deleteDog(@PathVariable("id") Long id) {
        return this.dogsService.deleteDog(id);
    }

    @Operation(
            summary = "Изменение данных о питомце",
            description = "Ищет питомца по id, затем заменяет в нем данные на те, что записаны в передаваемом dog."
                    + " Если в одном из параметров у dog указана пустая строка этот параметр не будет изменён.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Вернёт объект dogs с обновленными данными",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dogs.class)
                            )),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Недостаточно данных для обновления объекта dogs или объект с таким id не найден"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка при обновлении объекта dogs из-за неверного формата данных"
                    )}
    )
    @PutMapping("/update/{id}")
    public Dogs updateDog(@Parameter(
            description = "Id питомца",
            example = "1")
                          @PathVariable("id") Long id,
                          @Parameter(
                                  description = "Полные данные питомца",
                                  example = " {nick_name: 'Name', age : 5, info_dog : 'Описание привычек собаки'")
                          @RequestBody Dogs dog) {
        return this.dogsService.updateDog(id, dog);
    }

    @Operation(
            summary = "Загрузка фотографии питомца",
            description = "Принимает фотографию питомца и проверяет размер файла,"
                    + " при верном значении обновляет фотогафию у питомца с передаваемым id."
    )
    @PostMapping(value = "/{id}/load/fotoDog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Прикрепление фото собаки
    public ResponseEntity<String> uploadFotoDog(@PathVariable Long id, @RequestParam MultipartFile fotoDog) throws IOException {
        if (fotoDog.getSize() >= SIZE_FOTO_DOG) {         // Ограничение размера файла
            return ResponseEntity.badRequest().body("Файл большого размера");
        }
        dogsFotoService.uploadFotoDog(id, fotoDog);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Просмотр фотографии питомца"
    )
    @GetMapping(value = "/{id}/fotoDog")
    public void downloadFotoDog(@PathVariable Long id, HttpServletResponse response) throws IOException {
        DogsFoto dogsFoto = dogsFotoService.findFotoDog(id);
        Path path = Path.of(dogsFoto.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(dogsFoto.getMediaType());
            response.setContentLength((int) dogsFoto.getFileSize());
            is.transferTo(os);
        }
    }

}
