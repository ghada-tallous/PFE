package com.example.Pfe.service.impl;

import com.example.Pfe.entites.Categorie;
import com.example.Pfe.repository.CategorieRepository;
import com.example.Pfe.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategorieServiceImpl implements CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;


    @Override
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }


    @Override
    public Categorie getCategorie(Long id) {
        return categorieRepository.findById(id).get();
    }

    @Override
    public List<Categorie> getAllCategorie() {
        return (List<Categorie>) categorieRepository.findAll();
    }

    @Override
    public Categorie findCategorieByNom(String nom) {
        return categorieRepository.findByNom(nom);
    }

    @Override
    public void deleteById(Long id) {
        categorieRepository.deleteById(id);

    }

    @Override
    public Categorie updateCategorie(Categorie categorie) {
        return null;
    }

    @Override
    public void deleteCategorie(Categorie categorie) { categorieRepository.delete(categorie);

    }
}
