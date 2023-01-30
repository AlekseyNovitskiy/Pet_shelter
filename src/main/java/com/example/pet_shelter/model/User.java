package com.example.pet_shelter.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "users_dog_name")
    private String usersDogName;
    @Column(name = "users_phone_number")
    private String usersPhoneNumber;

    public User() {
    }
    public User(String name, String lastName, String dog, String phone) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsersDogName() {
        return usersDogName;
    }

    public void setUsersDogName(String usersDogName) {
        this.usersDogName = usersDogName;
    }

    public String getUsersPhoneNumber() {
        return usersPhoneNumber;
    }

    public void setUsersPhoneNumber(String usersPhoneNumber) {
        this.usersPhoneNumber = usersPhoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", usersDogName='" + usersDogName + '\'' +
                ", usersPhoneNumber='" + usersPhoneNumber + '\'' +
                '}';
    }
}
