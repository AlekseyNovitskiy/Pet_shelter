package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.Dogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogsRepository extends JpaRepository <Dogs,Long> {

}
