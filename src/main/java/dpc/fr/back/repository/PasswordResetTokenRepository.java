package dpc.fr.back.repository;

import dpc.fr.back.entity.PasswordResetToken;
import dpc.fr.back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(UserEntity user);
}

