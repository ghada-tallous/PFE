package com.example.Pfe.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.tomcat.util.http.parser.MediaType;

@Entity
@Data
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private MediaType type;


    // Enum pour définir les types de médias
    public enum MediaType {
        IMAGE, VIDEO
    }

    // Relation ManyToOne : Un média est lié à une seule publication
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "publicationId", nullable = false) // Clé étrangère publicationId
    private Publication publication;
}
