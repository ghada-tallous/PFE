package com.example.Pfe.repository;

import com.example.Pfe.entites.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionRepository extends JpaRepository <Interaction, Long> {
}
