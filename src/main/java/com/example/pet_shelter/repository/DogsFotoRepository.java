package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.DogsFoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogsFotoRepository extends JpaRepository<DogsFoto,Long> {


}
