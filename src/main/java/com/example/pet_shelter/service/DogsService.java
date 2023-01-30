package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.User;
import com.example.pet_shelter.repository.DogsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class DogsService {


    private DogsRepository dogsRepository;

    public Dogs createDog(Dogs dogs) {          // Сохранение введенной в URL новой собаки
        return this.dogsRepository.save(dogs);
    }

    public Optional<Dogs> findDog(Long dogId) {
        return this.dogsRepository.findById(dogId);
    }
}
