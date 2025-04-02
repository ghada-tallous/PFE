package com.example.Pfe.repository;

import com.example.Pfe.entites.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByPublicationId(Long publicationId);
}
