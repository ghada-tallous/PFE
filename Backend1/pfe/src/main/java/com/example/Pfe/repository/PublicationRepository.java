package com.example.Pfe.repository;

import com.example.Pfe.entites.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByCategorieId(Long categorieId);
}
