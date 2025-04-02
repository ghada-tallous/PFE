package com.example.Pfe.repository;

import com.example.Pfe.entites.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireRepository extends JpaRepository <Commentaire, Long> {
}
