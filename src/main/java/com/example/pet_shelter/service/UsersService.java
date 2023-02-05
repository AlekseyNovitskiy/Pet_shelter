package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.exceptions.UsersNullParameterValueException;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.regex.Pattern;

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
        if (user.getFirstName().isBlank() || user.getFirstName().isBlank()) {
            throw new UsersNullParameterValueException("Имя пользователя не указано");
        }
        if (user.getFirstName().isBlank() || user.getFirstName().isBlank()) {
            throw new UsersNullParameterValueException("Фамилия пользователя не указана");
        }
        // Форматирование телефона пользователя, если телефон указан неверно получаем null
        if (user.getUserPhoneNumber() != null) {
            user.setUserPhoneNumber(MatchingPhoneNumber(user.getUserPhoneNumber()));
        } else {
            throw new UsersNullParameterValueException("Телефон пользователя не указан или не соответствует формату");
        }
        if (!ValidityEmail(user.getUserEmail())) {
            throw new UsersNullParameterValueException("Почта пользователя не указана или не соответствует формату");
        }
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
        Users updateUser = usersRepository.findById(id).orElse(null);
        if (updateUser != null) {
            updateUser.setFirstName(user.getFirstName());
            updateUser.setLastName(user.getLastName());
            updateUser.setUserPhoneNumber(user.getUserPhoneNumber());
            updateUser.setUserEmail(user.getUserEmail());
        } else {
            throw new UsersNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта users");
        }
        return usersRepository.save(updateUser);
    }

    // Валидность номера телефона
    public String MatchingPhoneNumber(String telefone) {
        if (telefone.chars().filter(Character::isDigit).count() == 11) {
            String str = telefone.replaceAll("\\D+", "");
            return ("+" + str.substring(0, 1) + "(" + str.substring(1, 4) + ")" + str.substring(4, 7)
                    + "-" + str.substring(7, 9) + "-" + str.substring(9, 11));
        }
        return null;
    }

    // Валидность e-mail
    public boolean ValidityEmail(String eMail) {
        if (eMail == null) {
            return false;
        } else {
            String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$"; // В базе будет null
            return patternMatches(eMail, regexPattern);
        }
    }

    // Вспомогательный метод, соответствующий шаблону регулярных выражений
    public static boolean patternMatches(String TheStringBeingChecked, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(TheStringBeingChecked)
                .matches();
    }

}
