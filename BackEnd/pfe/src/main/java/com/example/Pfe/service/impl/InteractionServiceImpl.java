package com.example.Pfe.service.impl;

import com.example.Pfe.entites.Interaction;
import com.example.Pfe.repository.InteractionRepository;
import com.example.Pfe.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionServiceImpl implements InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;
    @Override
    public Interaction saveInteraction(Interaction interaction) {
        return interactionRepository.save(interaction);
    }

    @Override
    public Interaction getInteraction(Long id) {
        return interactionRepository.findById(id).get();
    }

    @Override
    public List<Interaction> getAllInteraction() {
        return (List<Interaction>) interactionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        interactionRepository.deleteById(id);

    }
}
