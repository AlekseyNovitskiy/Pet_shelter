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
        // Приведение в соответствие номера телефона
        user.setUserPhoneNumber(MatchingPhoneNumber(user.getUserPhoneNumber()));
        return this.usersRepository.save(user);
    }

    // Метод удаления пользователя
    public Users deleteUser(Long id) {
        Users deleteUser = usersRepository.findById(id).orElse(null);
        usersRepository.deleteById(id);
        return deleteUser;
    }

    // Метод изменения данных о пользователе
    public Users updateUser(Long id, Users user) {
        usersRepository.deleteById(id);
        // Приведение в соответствие номера телефона
        user.setUserPhoneNumber(MatchingPhoneNumber(user.getUserPhoneNumber()));
        return usersRepository.save(user);
    }

    // Проверка веденного номера телефона
    public String MatchingPhoneNumber(String telefone) {
        if (telefone.chars().filter(Character::isDigit).count() == 11) {
            String str = telefone.replaceAll("\\D+", "");
            return ("+" + str.substring(0, 1) + "(" + str.substring(1, 4) + ")" + str.substring(4, 7) + "-" + str.substring(7, 9) + "-" + str.substring(9, 11));
        } else throw new RuntimeException("Неправильный формат номера телефона");
    }

}
