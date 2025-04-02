package com.example.Pfe.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;


    // Relation OneToMany : Une catégorie peut regrouper plusieurs publications
    @JsonIgnore
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Publication> publications;
}
