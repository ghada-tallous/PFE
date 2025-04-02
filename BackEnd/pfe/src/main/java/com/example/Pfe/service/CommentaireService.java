package com.example.Pfe.service;

import com.example.Pfe.entites.Commentaire;

import java.util.List;

public interface CommentaireService {
    Commentaire saveCommentaire(Commentaire commentaire);
    Commentaire getCommentaire(Long id);
    List<Commentaire> getAllCommentaire();
    void deleteById(Long id);
    Commentaire updateCommentaire(Commentaire commentaire);
    void deleteCommentaire(Commentaire commentaire);
}
