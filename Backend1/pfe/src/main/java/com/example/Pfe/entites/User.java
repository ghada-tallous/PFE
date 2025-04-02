package com.example.Pfe.entites;

import com.example.Pfe.entites.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data // Annotation Lombok qui génère getters, setters, toString, equals, et hashCode
@Entity // Indique que cette classe est une entité mappée à une table dans la base de données
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(nullable = false, length = 100, unique = true) // Email unique et non nullable
    private String email;

    private String poste;
    private String photo;
    @Setter
    private String password;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry_date")
    private LocalDateTime resetTokenExpiryDate;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(name = "is_email_confirmed")
    private boolean isEmailConfirmed = false; // Par défaut, l'email n'est pas confirmé

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Relation OneToMany : Un utilisateur peut avoir plusieurs publications
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // "mappedBy" indique que la relation est gérée par le champ "user" dans Publication
    private List<Publication> publications;

    // Relation OneToMany : Un utilisateur peut poster plusieurs commentaires
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Commentaire> commentaires;

    // Relation OneToMany : Un utilisateur peut interagir avec plusieurs publications
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Interaction> interactions;


}