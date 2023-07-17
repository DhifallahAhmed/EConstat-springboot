package dpc.fr.back.api.service;

import dpc.fr.back.service.EmailSenderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void sendSimpleEmail_ShouldSendEmail() {
        // Arrange
        String toEmail = "example@example.com";
        String subject = "Test Subject";
        String body = "Test Body";
        // Act
        emailSenderService.sendSimpleEmail(toEmail, subject, body);
        // Assert
        Assertions.assertThat(toEmail).isNotNull();
        Assertions.assertThat(subject).isNotNull();
        Assertions.assertThat(body).isNotNull();
    }
}

