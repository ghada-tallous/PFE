package com.example.Pfe.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String titre;
    private String contenu;
    private String lien;
    private String localisation;
    private boolean active = true; // Par défaut, la publication est active

    public Publication(String titre, String contenu, String lien, boolean active, Categorie categorie) {
        this.titre = titre;
        this.contenu = contenu;
        this.lien = lien;
        this.active = active;

    }

    // Relation ManyToOne : Une publication est liée à un seul utilisateur
    @ManyToOne
    @JoinColumn(name = "user_id") // Clé étrangère userId dans la table publications
    private User user;


    // Relation ManyToOne : Une publication appartient à une seule catégorie
    @ManyToOne
    @JoinColumn(name = "categorie_id") // Clé étrangère categoryId
    private Categorie categorie;


    // Relation OneToMany : Une publication peut avoir plusieurs commentaires
    @JsonIgnore
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    private List<Commentaire> commentaires;

    // Relation OneToMany : Une publication peut avoir plusieurs interactions (évaluations)
    @JsonIgnore
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    private List<Interaction> interactions;

    // Relation OneToMany : Une publication peut avoir plusieurs médias (images/vidéos)
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    private List<Media> medias;
}