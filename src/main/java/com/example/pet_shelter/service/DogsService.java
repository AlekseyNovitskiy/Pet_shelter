package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.DogsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DogsService {

    private final DogsRepository dogsRepository;
    public DogsService(DogsRepository dogsRepository) {
        this.dogsRepository = dogsRepository;
    }

    /**
     * <b> Добавление питомца в БД</b>
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     * @param dog класс сущности
     * @return Возращает сохраненного питомца
     */
    public Dogs createDog(Dogs dog) {           // Сохранение введенной в URL новой собаки
        return dogsRepository.save(dog);
    }


    /**
     * <b> Удаление питомца из БД</b>
     * <br> Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор питомца
     * @return Возращает удаленного питомца
     */
    public Dogs deleteDog(Long id) {            // Удаление введенной в URL собаки
        Dogs deleteDog = dogsRepository.findById(id).orElse(null);
        dogsRepository.deleteById(id);
        return deleteDog;
    }

    /**
     * <b> Изменение информации питомца в БД</b>
     * <br> Используется метод репозитория {@link JpaRepository#save(Object)}
     * <br> Используется метод репозитория {@link JpaRepository#deleteById(Object)}
     * @param id идентификатор питомца
     * @param dog класс Dogs
     * @return Возращает измененного питомца
     */
    public Dogs updateDog(Long id,Dogs dog) {    // Обновление введенной в URL собаки
        dogsRepository.deleteById(id);
        return dogsRepository.save(dog);
    }

    /** Поиск информации питомца в БД по его id идентификатору
     *
     * @param dogId идентификатор питомца
     * @return Возращает питомца из БД по id идентификатору питомца, может вернуть Null
     */
    // Поиск всех питомцев
    public Optional<Dogs> findDog(Long dogId) {
        return this.dogsRepository.findById(dogId);
    }
}
