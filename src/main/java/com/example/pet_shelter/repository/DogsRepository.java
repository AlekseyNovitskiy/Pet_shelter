package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.Dogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogsRepository extends JpaRepository <Dogs,Long> {

    @Override
    Optional<Dogs> findById(Long aLong);

}
