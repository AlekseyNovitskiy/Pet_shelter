package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.DogsRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class DogsService {

    private final DogsRepository dogsRepository;

    public DogsService(DogsRepository dogsRepository) {
        this.dogsRepository = dogsRepository;
    }

    public Collection<Dogs> getAllDogs() {
        return this.dogsRepository.findAll();
    }

    /**
     * <i>Заносит в базу созданный объект питомца</i>
     *
     * @param dog
     * @return
     */
    public Dogs createDog(Dogs dog) {
        if (dog.getNickname().isBlank() || dog.getNickname().isBlank()) {
            throw new DogNullParameterValueException("Кличка питомца не указана");
        }
        return dogsRepository.save(dog);
    }

    /**
     * <i>Удаляет из базы питомца по id</i>
     *
     * @param id
     * @return
     */
    public Dogs deleteDog(Long id) {
        Dogs deleteDog = dogsRepository.findById(id).orElse(null);
        dogsRepository.deleteById(id);
        return deleteDog;
    }

    /**
     * <i>Заменяет старые параметры питомца на переданные.
     * Если объект по id не найден будет выкинуто исключение DogNullParameterValueException.
     * При отсутсвии одного из полей у передаваемого объекта dog будет выкинуто исключение NullPointerException.
     * </i>
     *
     * @param id
     * @param dog
     * @return
     */
    public Dogs updateDog(Long id, Dogs dog) {
        Dogs updateDog = dogsRepository.findById(id).orElse(null);
        if (updateDog != null) {
                updateDog.setNickname(dog.getNickname());
                updateDog.setAge(dog.getAge());
                updateDog.setInfoDog(dog.getInfoDog());
        } else {
            throw new DogNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта dogs");
        }
        return dogsRepository.save(updateDog);
    }
}
