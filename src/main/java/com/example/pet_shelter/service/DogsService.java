package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.repository.DogsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DogsService {

    private final DogsRepository dogsRepository;
    public DogsService(DogsRepository dogsRepository) {
        this.dogsRepository = dogsRepository;
    }


    public Dogs createDog(Dogs dog) {           // Сохранение введенной в URL новой собаки
        return dogsRepository.save(dog);
    }

    public Dogs deleteDog(Long id) {            // Удаление введенной в URL собаки
        Dogs deleteDog = dogsRepository.findById(id).orElse(null);
        dogsRepository.deleteById(id);
        return deleteDog;
    }

    public Dogs updateDog(Long id,Dogs dog) {    // Обновление введенной в URL собаки
        dogsRepository.deleteById(id);
        return dogsRepository.save(dog);
    }


    // Поиск всех питомцев
    public Optional<Dogs> findDog(Long dogId) {
        return this.dogsRepository.findById(dogId);
    }
}
