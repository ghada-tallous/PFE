package com.example.Pfe.service;

import com.example.Pfe.entites.Interaction;

import java.util.List;

public interface InteractionService {

    Interaction saveInteraction(Interaction interaction);
    Interaction getInteraction(Long id);
    List<Interaction> getAllInteraction();
    void deleteById(Long id);
}
