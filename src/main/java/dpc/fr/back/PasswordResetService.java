package dpc.fr.back;

import dpc.fr.back.entity.PasswordResetToken;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.PasswordResetTokenRepository;
import dpc.fr.back.repository.UserRepository;
import dpc.fr.back.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void sendPasswordResetEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            // Generate a unique password reset token
            String token = generateToken();

            // Create a password reset token entity and associate it with the user
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setUser(user);
            passwordResetTokenRepository.save(passwordResetToken);

            // Send the password reset email containing the token to the user's email address
            sendEmail(user.getEmail(), token);
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken != null) {
            UserEntity user = passwordResetToken.getUser();
            user.setPassword(newPassword);
            userRepository.save(user);

            // Delete the password reset token after resetting the password
            passwordResetTokenRepository.delete(passwordResetToken);
        }
    }

    private String generateToken() {
        return null;
    }

    private void sendEmail(String email, String token) {
        // Implement your email sending logic here
    }
}

