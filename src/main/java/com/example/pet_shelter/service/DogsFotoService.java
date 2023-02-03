package com.example.pet_shelter.service;

import com.example.pet_shelter.model.Dogs;
import com.example.pet_shelter.model.DogsFoto;
import com.example.pet_shelter.repository.DogsFotoRepository;
import com.example.pet_shelter.repository.DogsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class DogsFotoService {

    private final DogsRepository dogsRepository;
    private final DogsFotoRepository dogsFotoRepository;

    public DogsFotoService(DogsRepository dogsRepository, DogsFotoRepository dogsFotoRepository) {
        this.dogsRepository = dogsRepository;
        this.dogsFotoRepository = dogsFotoRepository;
    }

    @Value("${dir.path.FotoDogs}")
    private String FotoDogsDir;      // Директория фото собак

    // Загрузка фото собаки в базу данных и на диск
    public void uploadFotoDog(Long id, MultipartFile file) throws IOException {

        Dogs dog = dogsRepository.findById(id).orElseThrow();
        Path filePath = Path.of(FotoDogsDir, id + "." + getExtensions(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent()); // Создание директории если ее нет
        Files.deleteIfExists(filePath);  // Если такой файл существует, то мы его удаляем
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 2048);
                BufferedOutputStream bos = new BufferedOutputStream(os, 2048);
        ) {
            bis.transferTo(bos);
        }
        DogsFoto dogsFoto = dogsFotoRepository.findById(id).orElseGet(DogsFoto::new);
        dogsFoto.setDog(dog);
        dogsFoto.setFilePath(filePath.toString());
        dogsFoto.setFileSize(file.getSize());
        dogsFoto.setMediaType(file.getContentType());
        dogsFoto.setFotoDog(file.getBytes());
        dogsFotoRepository.save(dogsFoto);
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);

    }
    // Поиск собаки по id
    public Dogs findDog(Long gogId) {
        return dogsRepository.findById(gogId).orElseThrow();
    }

    // Поиск фото собаки по id
    public DogsFoto findFotoDog(Long id) {
        return dogsFotoRepository.findById(id).orElseThrow();
    }

}
