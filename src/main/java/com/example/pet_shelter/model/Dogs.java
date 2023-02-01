package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
@Table(name = "dogs")
public class Dogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;  // Идентификатор

    @Column(name = "nick_name")    // Кличка
    private String nickname;

    @Column(name = "age")          // Возраст
    private int age;

    @Column(name = "info_dog")     // Информация о собаке
    private String infoDog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {                // Проверка на отрицательное число
            age *= -1;                 // Инвентаризация числа
        } else if (age > 30) {       // Проверка на максимальный возраст
            age = 0;
        }
        this.age = age;
    }

    public String getInfoDog() {
        return infoDog;
    }

    public void setInfoDog(String infoDog) {
        this.infoDog = infoDog;
    }

}
