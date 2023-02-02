package com.example.pet_shelter.service;

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
        // Приведение в соответствие номера телефона
        if (user.getUserPhoneNumber() != null) {
            user.setUserPhoneNumber(MatchingPhoneNumber(user.getUserPhoneNumber()));
        }
        if (!ValidityEmail(user.getUserEmail())) {  // Если ошибка в e-mail, то просто записывать не будем
            user.setUserEmail(null);
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
        usersRepository.deleteById(id);
        // Приведение в соответствие телефона в случай количества символов не равное 11 номер телефона не записываем
        if (user.getUserPhoneNumber() != null) {
            user.setUserPhoneNumber(MatchingPhoneNumber(user.getUserPhoneNumber()));
        }
        if (!ValidityEmail(user.getUserEmail())) {  // Если ошибка в e-mail, то просто записывать не будем
            user.setUserEmail(null);
        }
        return usersRepository.save(user);
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
            String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
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
