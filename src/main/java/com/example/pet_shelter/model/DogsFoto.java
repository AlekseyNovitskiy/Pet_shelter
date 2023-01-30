package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
@Table(name = "dogs")
public class DogsFoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dogId;       // Идентификатор

    private String filePath;  // Название файла

    private long fileSize;    // Размер файла

    private String MediaType;   // Тип файла

    @Lob
    private  byte[] fotoDog;  // массив для хранения фотографии

    @OneToOne                 // связь один к одному
    private Dogs dog;

    public Long getDogId() {
        return dogId;
    }

    public void setDogId(Long dogId) {
        this.dogId = dogId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFotoDog() {
        return fotoDog;
    }

    public void setFotoDog(byte[] fotoDog) {
        this.fotoDog = fotoDog;
    }

    public Dogs getDog() {
        return dog;
    }

    public void setDog(Dogs dog) {
        this.dog = dog;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

}
