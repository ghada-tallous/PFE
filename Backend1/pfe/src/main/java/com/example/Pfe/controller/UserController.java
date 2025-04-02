package com.example.Pfe.controller;


import com.example.Pfe.entites.*;
import com.example.Pfe.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService userService;
    private final PublicationService publicationService;
    private final CommentaireService commentaireService;
    private final InteractionService interactionService;
    private final MediaService mediaService;
    private final CategorieService categorieService;


    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ok("Hello User");
    }

    // Gestion utilisateurs
    /*@GetMapping("/{email}")
    public User getUtilisateurByEmail(@PathVariable String email) {

        return userService.getUtilisateurByEmail(email);
    }*/
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUtilisateur(user);
        return ResponseEntity.ok(updatedUser);
    }

   /* @GetMapping("/{id}")
    public User getUtilisateur(@PathVariable Long id) {
        return userService.getUtilisateur(id);
    }
/*
    @GetMapping
    public List<User> getAllUtilisateurs() {
        return userService.getAllUtilisateurs();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
    @PutMapping("/update")
    public User updateUtilisateur(@RequestBody User user) {
        return userService.updateUtilisateur(user);
    }

    @PostMapping (consumes ="application/json")
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        Publication createdPublication = publicationService.createPublication(publication);
        return ResponseEntity.ok(createdPublication);
    }

    @PostMapping("/upload")
    public ResponseEntity<Media> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("publicationId") Long publicationId) throws IOException {
        System.out.println("Requête POST reçue pour uploader un média : " + file.getOriginalFilename());
        Media savedMedia = mediaService.saveMedia(file, publicationId);
        System.out.println("Média sauvegardé : " + savedMedia);
        return ResponseEntity.ok(savedMedia);
    }*/

    // Gestion des publications

    @PostMapping(value = "/publications/with-media", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPublicationWithMedia(
            @RequestParam("titre") String titre,
            @RequestParam("contenu") String contenu,
            @RequestParam(value = "lien", required = false) String lien,
            @RequestParam(value = "localisation", required = false) String localisation,
            @RequestParam(value = "active", required = false, defaultValue = "true") boolean active,
            @RequestParam("categorieId") Long categorieId,
            //   @RequestParam("userId") Long userId,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        try {
            // Crée la publication
            Publication publication = new Publication();
            publication.setTitre(titre);
            publication.setContenu(contenu);
            publication.setLien(lien);
            publication.setLocalisation(localisation);
            publication.setActive(active);
            publication.setDate(new Date()); // Définit la date actuelle
/*
            // Récupère l'utilisateur
            User user = userService.getUtilisateur(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body("Utilisateur avec l'ID " + userId + " non trouvé.");
            }
            publication.setUser(user);
*/
            // Récupère l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
            }
            publication.setUser(user);
            // Récupère la catégorie
            Categorie categorie = categorieService.getCategorie(categorieId);
            if (categorie == null) {
                return ResponseEntity.badRequest().body("Catégorie avec l'ID " + categorieId + " non trouvée.");
            }
            publication.setCategorie(categorie);

            // Sauvegarde la publication
            Publication createdPublication = publicationService.createPublication(publication);

            // Si des fichiers sont fournis, associe-les à la publication
            if (files != null && files.length > 0) {
                mediaService.saveMultipleMedia(files, createdPublication.getId());
            }

            return ResponseEntity.ok(createdPublication);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la publication : " + e.getMessage());
        }
    }/*
    @PostMapping(value = "/publications/with-media", consumes = {"multipart/form-data"})
    public ResponseEntity<Publication> createPublicationWithMedia(
            @RequestParam("contenu") String contenu,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        try {
            // Crée la publication
            Publication publication = new Publication();
            publication.setContenu(contenu);

            User user = userService.getUtilisateur(userId);
            if (user == null) {
                return ResponseEntity.badRequest().body(null);
            }
            publication.setUser(user);

            Publication createdPublication = publicationService.createPublication(publication);

            // Si des fichiers sont fournis, associe-les à la publication
            if (files != null && files.length > 0) {
                mediaService.saveMultipleMedia(files, createdPublication.getId());
            }

            return ResponseEntity.ok(createdPublication);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }*/
    /*@PostMapping ("/publications")
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        Publication createdPublication = publicationService.createPublication(publication);
        return ResponseEntity.ok(createdPublication);
    }*/

    @PutMapping(value = "/publications/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updatePublication(
            @PathVariable Long id,
            @RequestParam("titre") String titre,
            @RequestParam("contenu") String contenu,
            @RequestParam(value = "lien", required = false) String lien,
            @RequestParam(value = "localisation", required = false) String localisation,
            @RequestParam(value = "active", required = false, defaultValue = "true") boolean active,
            @RequestParam(value = "categorieId", required = false) Long categorieId,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        try {
            // Récupère la publication existante
            Publication existingPublication = publicationService.getPublicationById(id);
            if (existingPublication == null) {
                return ResponseEntity.badRequest().body("Publication avec l'ID " + id + " non trouvée.");
            }
            // Vérifie que l'utilisateur connecté est l'auteur de la publication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
            }
            if (!existingPublication.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à modifier cette publication.");
            }
            // Met à jour les champs de la publication
            existingPublication.setTitre(titre);
            existingPublication.setContenu(contenu);
            existingPublication.setLien(lien);
            existingPublication.setLocalisation(localisation);
            existingPublication.setActive(active);
            // Récupère la catégorie
            Categorie categorie = categorieService.getCategorie(categorieId);
            if (categorie == null) {
                return ResponseEntity.badRequest().body("Catégorie avec l'ID " + categorieId + " non trouvée.");
            }
            existingPublication.setCategorie(categorie);
            // Sauvegarde la publication mise à jour
            Publication updatedPublication = publicationService.updatePublication(existingPublication);
            // Si de nouveaux fichiers sont fournis, met à jour les médias
            if (files != null && files.length > 0) {
                // Option 1 : Supprime les anciens médias avant d'ajouter les nouveaux
                mediaService.deleteMediaByPublicationId(updatedPublication.getId());
                // Ajoute les nouveaux médias
                mediaService.saveMultipleMedia(files, updatedPublication.getId());
            }
            return ResponseEntity.ok(updatedPublication);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour de la publication : " + e.getMessage());
        }
    }

    @DeleteMapping("/publications/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publications")
    public ResponseEntity<List<Publication>> getAllPublications() {
        List<Publication> publications = publicationService.getAllPublications();
        return ResponseEntity.ok(publications);
    }

    //recherche par categories Id
    @GetMapping("/publications/categorie/{categorieId}")
    public ResponseEntity<?> getPublicationsByCategorie(@PathVariable Long categorieId) {
        Categorie categorie = categorieService.getCategorie(categorieId);
        if (categorie == null) {
            return ResponseEntity.badRequest().body("Catégorie avec l'ID " + categorieId + " non trouvée.");
        }
        List<Publication> publications = publicationService.findPublicationsByCategorieId(categorieId);
        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publications);
    }

    //categorie
    @GetMapping("/categories")
    public ResponseEntity<List<Categorie>> getAllCategorie() {
        List<Categorie> categories = categorieService.getAllCategorie();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{nom}")
    public ResponseEntity<Categorie> getCategorieByNom(@PathVariable String nom) {
        Categorie categorie = categorieService.findCategorieByNom(nom);
        if (categorie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categorie);
    }

    // Gestion des médias

    @PostMapping("/media/upload")
    public ResponseEntity<Media> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("publicationId") Long publicationId) throws IOException {
        System.out.println("Requête POST reçue pour uploader un média : " + file.getOriginalFilename());
        Media savedMedia = mediaService.saveMedia(file, publicationId);
        System.out.println("Média sauvegardé : " + savedMedia);
        return ResponseEntity.ok(savedMedia);
    }

    @PostMapping("/upload-multiple/{publicationId}")
    public ResponseEntity<List<Media>> uploadMultipleMedia(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable Long publicationId) {
        try {
            List<Media> savedMedia = mediaService.saveMultipleMedia(files, publicationId);
            return ResponseEntity.ok(savedMedia);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Media> getMedia(@PathVariable Long id) {
        Media media = mediaService.getMedia(id);
        return ResponseEntity.ok(media);
    }

    @DeleteMapping("/media/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    // Gestion des commentaires

    @PostMapping("/commentaires")
    public Commentaire saveCommentaire(@RequestBody Commentaire commentaire) {
        return commentaireService.saveCommentaire(commentaire);
    }

    @GetMapping("/commentaires/{id}")
    public Commentaire getCommentaire(@PathVariable Long id) {
        return commentaireService.getCommentaire(id);
    }

    @GetMapping("/commentaires")
    public List<Commentaire> getAllCommentaire() {
        return commentaireService.getAllCommentaire();
    }

    @DeleteMapping("/commentaires/{id}")
    public ResponseEntity<Void> deletecommentaires(@PathVariable Long id) {
        commentaireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Gestion des interactions

    @PostMapping("/interactions")
    public Interaction saveInteraction(@RequestBody Interaction interaction) {
        return interactionService.saveInteraction(interaction);
    }

    @GetMapping("/interactions/{id}")
    public Interaction getInteraction(@PathVariable Long id) {
        return interactionService.getInteraction(id);
    }

    @GetMapping("/interactions")
    public List<Interaction> getAllInteraction() {
        return interactionService.getAllInteraction();
    }

    @DeleteMapping("/interactions/{id}")
    public ResponseEntity<Void> deleteinteractions(@PathVariable Long id) {
        interactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

