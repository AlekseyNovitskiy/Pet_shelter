package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.UsersRepository;
import org.springframework.data.jpa.repository.JpaRepository;
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

    /**
     * <b> Добавление User в БД</b>
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param user класс сущности
     * @return Возращает сохраненного User
     */
    // Метод добавление пользователя
    public Users createUser(Users user) {
        // Приведение в соответствие номера телефона
        if (user.getUserPhoneNumber() != null) {
            user.setUserPhoneNumber(matchingPhoneNumber(user.getUserPhoneNumber()));
        }
        if (!ValidityEmail(user.getUserEmail())) {  // Если ошибка в e-mail, то просто записывать не будем
            user.setUserEmail(null);
        }
        return this.usersRepository.save(user);
    }

    /**
     * <b> Удаление User из БД</b>
     * <br> Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор пользователя, не может быть {@code null}
     * @return Возращает сохраненного User
     */
    // Метод удаления пользователя
    public Users deleteUser(Long id) {
        Users deleteUser = usersRepository.findById(id).orElse(null);
        usersRepository.deleteById(id);
        return deleteUser;
    }

    /**
     * <b> Изменение User в БД</b>
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param id идентификатор пользователя, не может быть {@code null}
     * @param  user класс сущности
     * @return Возращает измененного User
     */
    // Метод изменения данных о пользователе
    public Users updateUser(Long id, Users user) {
        usersRepository.deleteById(id);
        // Приведение в соответствие телефона в случай количества символов не равное 11 номер телефона не записываем
        if (user.getUserPhoneNumber() != null) {
            user.setUserPhoneNumber(matchingPhoneNumber(user.getUserPhoneNumber()));
        }
        if (!ValidityEmail(user.getUserEmail())) {  // Если ошибка в e-mail, то просто записывать не будем
            user.setUserEmail(null);
        }
        return usersRepository.save(user);
    }

    /**
     * <b>Приведение номера телефона пользователя к шаблону</b>
     * @param telefone строка содержащая телефон пользователя
     * @return Возвращает телефон по заданному шаблону +7(xxx)xxx-xx-xx
     */
    // Валидность номера телефона
    public String matchingPhoneNumber(String telefone) {
        // Проверка строки на содержание там 11 цифр
        if (telefone.chars().filter(Character::isDigit).count() == 11) {
            // Оставляем только цифры
            String str = telefone.replaceAll("\\D+", "");
            // Формирование строки по шаблону  +7(xxx)xxx-xx-xx
            return ("+" + str.substring(0, 1) + "(" + str.substring(1, 4) + ")" + str.substring(4, 7)
                    + "-" + str.substring(7, 9) + "-" + str.substring(9, 11));
        }
        return null;
    }

    /**
     * <b>Приведение номера телефона пользователя к шаблону</b>
     * * <br> Используется метод репозитория {@link UsersService#patternMatches(String, String)}
     * где в качестве первого параметра регулярное выражение, второй - передаваемая строка для обработки
     * @param eMail строка содержащая e-mail пользователя
     * @return Возвращает boolean true- e-mail валидный false не валидный
     */
    // Валидность e-mail
    public boolean ValidityEmail(String eMail) {
        if (eMail == null) {
            return false;
        } else {
            String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            return patternMatches(eMail, regexPattern);
        }
    }

    /**
     *
     * @param theStringBeingChecked - входное регулярное выражение
     * @param regexPattern - входное строковая переменная для обработки регулярным выражением
     * @return boolean true -строка соответствует регулярному выражению, false - не соответствует
     */
    // Вспомогательный метод, соответствующий шаблону регулярных выражений
    public static boolean patternMatches(String theStringBeingChecked, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(theStringBeingChecked)
                .matches();
    }

}
