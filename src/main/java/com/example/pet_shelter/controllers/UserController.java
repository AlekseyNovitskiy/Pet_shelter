package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(summary = "Получение всех пользователей",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о Users найдена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))},tags = "USER")
    // Получение всех пользователей
    @GetMapping("/getALL")
    public Collection<Users> getAllUsers() {
        return this.usersService.getAllUsers();
    }


    @Operation(summary = "Внесение данных о новой пользователе",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о User занесена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Users.class))
                    ))},tags = "USER")
    // Добавление данных о новом пользователе
    @PostMapping("/create")
    public Users createUser(@RequestBody Users user) {
        return this.usersService.createUser(user);
    }

    @Operation(summary = "Удаление данных о пользователе",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о User удалена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    ))},tags = "USER")
    // Удаление данных о пользователе
    @DeleteMapping("/delete/{id}")
    public Users deleteDog(@Parameter(description = "Id User", example = "1") @PathVariable("id") Long id) {
        return this.usersService.deleteUser(id);
    }


    @Operation(summary = "Изменение данных о пользователе",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Информация о User изменена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Users.class))
                    ))},tags = "USER")
    // Изменение данных о пользователе
    @PutMapping("/update/{id}")
    public Users deleteDog(@Parameter(description = "Id User", example = "1") @PathVariable("id") Long id, @RequestBody Users user) {
        return this.usersService.updateUser(id, user);
    }

}
