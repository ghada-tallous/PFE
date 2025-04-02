package com.example.Pfe.repository;



import com.example.Pfe.entites.Role;
import com.example.Pfe.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
    User findByRole(Role email);
   Optional<User> findByResetToken(String resetToken);
    Optional<User> findByConfirmationToken(String confirmationToken);

}

