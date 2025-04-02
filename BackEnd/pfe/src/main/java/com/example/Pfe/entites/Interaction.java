package com.example.Pfe.entites;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    // Relation ManyToOne : Une interaction est liée à une seule publication
    @ManyToOne
    @JoinColumn(name = "publicationId", nullable = false) // Clé étrangère publicationId
    private Publication publication;

    // Relation ManyToOne : Une interaction est faite par un seul utilisateur
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) // Clé étrangère userId
    private User user;
}
