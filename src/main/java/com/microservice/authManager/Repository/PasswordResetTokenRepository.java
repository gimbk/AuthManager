package com.microservice.authManager.Repository;

import com.microservice.authManager.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    @Query(value = "SELECT t.* FROM password_reset_token t " +
            "WHERE t.token = :token", nativeQuery = true)
    PasswordResetToken findByToken(String token);
}
