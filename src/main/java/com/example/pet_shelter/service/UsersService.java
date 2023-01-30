package com.example.pet_shelter.service;

import com.example.pet_shelter.model.User;
import com.example.pet_shelter.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsersService {

    private final UsersRepository repository;

    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public Collection<User> getAllUsers() {
        return this.repository.findAll();
    }
}
