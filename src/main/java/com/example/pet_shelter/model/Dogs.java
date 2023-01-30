package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
@Table(name = "dogs")
public class Dogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dogId;       // Идентификатор

    private String nickname;  // Кличка собаки

    private String age;       // Возраст

}
