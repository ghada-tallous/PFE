package com.example.Pfe.repository;

import com.example.Pfe.entites.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    // Méthode pour trouver une catégorie par son nom
    Categorie findByNom(String nom);

    // Méthode pour vérifier si une catégorie existe par son nom
    boolean existsByNom(String nom);

    // Méthode pour supprimer une catégorie par son ID
    void deleteById(Long id);
}
