package com.example.pet_shelter.controllers;

import com.example.pet_shelter.model.User;
import com.example.pet_shelter.service.UsersService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    //получение всех юзеров для проверки работоспособности бд и методов
    @GetMapping
    public Collection<User> getAllUsers(){
       return this.service.getAllUsers();
    }

    @PostMapping("/create")
    public User createUser (@RequestBody User user) {
        return this.service.createUser(user);
    }

}
