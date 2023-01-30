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

    @Autowired
    private DogsService dogsService;

    @Autowired
    DogsRepository dogsRepository;

    @Value("${dir.path.FotoDogs}")
    private String FotoDogsDir;      // Директория фото собак

    public void uploadFotoDog(Long dogId, MultipartFile foto) throws IOException {
        Optional<Dogs> dogs = dogsService.findDog(dogId);
        Path filePath = Path.of(FotoDogsDir, dogId + "." + getExtensions(foto.getOriginalFilename()));
        Files.createDirectories(filePath.getParent()); // Создание директории если ее нет
        Files.deleteIfExists(filePath);  // Если такой файл существует, то мы его удаляем
        try (
                InputStream is = foto.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }


//        Dogs dog = findDog(dogId);
//        DogsFoto dogsFoto = new dogsFoto;
//        dogsFoto.setDog(dogsFoto);
//        dogsFoto.setFilePath(filePath.toString());
//        dogsFoto.setFileSize(foto.getSize());
//        dogsFoto.setMediaType(foto.getContentType());
//        dogsFotoRepository.save(dogsFoto);
    }

    public Dogs findDog(Long gogId) {
        return dogsRepository.findById(gogId).orElseThrow();
    }


    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);

    }

}
