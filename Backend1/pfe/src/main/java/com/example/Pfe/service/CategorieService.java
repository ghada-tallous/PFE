package com.example.Pfe.service;

import com.example.Pfe.entites.Categorie;

import java.util.List;

public interface CategorieService {

    Categorie saveCategorie(Categorie categorie);
    Categorie updateCategorie(Categorie categorie);
    void deleteCategorie(Categorie categorie);
    void deleteById(Long id);
    Categorie getCategorie(Long id);
    List<Categorie> getAllCategorie();
    Categorie findCategorieByNom(String nom);

}
