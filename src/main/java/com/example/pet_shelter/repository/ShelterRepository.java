package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.ReportUsers;
import com.example.pet_shelter.model.Shelters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterRepository extends JpaRepository<Shelters, Long> {
}
