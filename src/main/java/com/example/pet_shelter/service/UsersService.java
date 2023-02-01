package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Collection<Users> getAllUsers() {
        return this.usersRepository.findAll();
    }

    // Метод добавление пользователя
    public Users createUser(Users user) {
        return this.usersRepository.save(user);
    }

    // Метод удаления пользователя
    public Users deleteUser(Long id) {
        Users deleteUser = usersRepository.findById(id).orElse(null);
        usersRepository.deleteById(id);
        return deleteUser;
    }

    // Метод изменения данных о пользователе
    public Users updateUser(Long id,Users user) {
        usersRepository.deleteById(id);
        return usersRepository.save(user);
    }

}
