package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    // Получение всех пользователей
    @GetMapping("/getALL")
    @Operation(summary = "Получение всех пользователей")
    public Collection<Users> getAllUsers() {
        return this.usersService.getAllUsers();
    }

    // Внесение данных о новом пользователе
    @PostMapping("/create")
    @Operation(summary = "Внесение данных о новой пользователе")
    public Users createUser(@RequestBody Users user) {
        return this.usersService.createUser(user);
    }

    // Удаление данных о пользователе
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление данных о пользователе")
    public Users deleteDog(@PathVariable("id") Long id) {
        return this.usersService.deleteUser(id);
    }

    // Изменение данных о пользователе
    @PutMapping("/update/{id}")
    @Operation(summary = "Изменение данных о пользователе")
    public Users deleteDog(@PathVariable("id") Long id, @RequestBody Users user) {
        return this.usersService.updateUser(id, user);
    }

}
