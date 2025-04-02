package com.example.Pfe.entites;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;


    // Relation ManyToOne : Un commentaire est lié à une seule publication
    @ManyToOne
    @JoinColumn(name = "publicationId", nullable = false) // Clé étrangère publicationId
    private Publication publication;

    // Relation ManyToOne : Un commentaire est posté par un seul utilisateur
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Clé étrangère userId
    private User user;
}
