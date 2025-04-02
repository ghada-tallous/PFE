package com.example.Pfe.service.impl;

import com.example.Pfe.entites.Commentaire;
import com.example.Pfe.repository.CommentaireRepository;
import com.example.Pfe.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentaireServiceImpl implements CommentaireService {
    @Autowired

    private CommentaireRepository commentaireRepository;

    @Override
    public Commentaire saveCommentaire(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    @Override
    public Commentaire getCommentaire(Long id) {
        return commentaireRepository.findById(id).get();
    }

    @Override
    public List<Commentaire> getAllCommentaire() {
        return (List<Commentaire>) commentaireRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        commentaireRepository.deleteById(id);

    }

    @Override
    public Commentaire updateCommentaire(Commentaire commentaire) {
        return null;
    }

    @Override
    public void deleteCommentaire(Commentaire commentaire) {

    }
}
