package dpc.fr.back.api.controller;

import dpc.fr.back.controller.UserController;
import dpc.fr.back.entity.Car;
import dpc.fr.back.entity.CarBrand;
import dpc.fr.back.entity.ChangePasswordRequest;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.UserRepository;
import dpc.fr.back.service.EmailSenderService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSenderService senderService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testDeleteUser_UserExists() {
        // Given
        int userId = 1;
        UserEntity user = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<String> response = userController.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).delete(user);
        Assertions.assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteUser_UserNotExists() {
        // Given
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<String> response = userController.deleteUser(userId);

        // Then
        verify(userRepository, never()).delete(any(UserEntity.class));
        Assertions.assertEquals("User Not Found", response.getBody());
    }

    @Test
    public void testFindAllUsers() {
        // Given
        List<UserEntity> users = Arrays.asList(new UserEntity(), new UserEntity());
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserEntity> result = userController.findAllusers();

        // Then
        Assertions.assertEquals(users, result);
    }

    @Test
    public void testFindUserById_UserExists() {
        // Given
        int userId = 1;
        UserEntity user = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        UserEntity result = userController.findUserByid(userId);

        // Then
        Assertions.assertEquals(user, result);
    }

    @Test
    public void testFindUserById_UserNotExists() {
        // Given
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        UserEntity result = userController.findUserByid(userId);

        // Then
        Assertions.assertNull(result);
    }

    @Test
    public void testUpdateUser_UserExists() {
        // Given
        int userId = 1;
        UserEntity user = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserEntity updatedUser = new UserEntity();
        updatedUser.setFullName("John Doe");

        // When
        ResponseEntity<String> response = userController.updateUser(userId, updatedUser);

        // Then
        verify(userRepository, times(1)).save(user);
        Assertions.assertEquals("User updated successfully", response.getBody());
    }

    @Test
    public void testUpdateUser_UserNotExists() {
        // Given
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserEntity updatedUser = new UserEntity();
        updatedUser.setFullName("John Doe");

        // When
        ResponseEntity<String> response = userController.updateUser(userId, updatedUser);

        // Then
        verify(userRepository, never()).save(any(UserEntity.class));
        Assertions.assertEquals("User Not Found", response.getBody());
    }

    @Test
    public void testChangePassword_UserExistsAndCorrectPassword() {
        // Given
        String email = "test@example.com";
        String currentPassword = "currentPass";
        String newPassword = "newPass";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(currentPassword));

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        // When
        ResponseEntity<String> response = userController.changePassword(email, request);

        // Then
        verify(userRepository, times(1)).save(user);
        Assertions.assertEquals("Password changed successfully", response.getBody());
    }

    @Test
    public void testChangePassword_UserNotExists() {
        // Given
        String email = "test@example.com";
        ChangePasswordRequest request = new ChangePasswordRequest();

        when(userRepository.findByEmail(email)).thenReturn(null);

        // When
        ResponseEntity<String> response = userController.changePassword(email, request);

        // Then
        verify(userRepository, never()).save(any(UserEntity.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testChangePassword_InvalidCurrentPassword() {
        // Given
        String email = "test@example.com";
        String currentPassword = "currentPass";
        String newPassword = "newPass";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("otherPassword"));

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // When
        ResponseEntity<String> response = userController.changePassword(email, request);

        // Then
        verify(userRepository, never()).save(any(UserEntity.class));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Invalid current password", response.getBody());
    }
    @Test
    public void testVerification_UserExistsAndCorrectOTP() {
        // Given
        String email = "test@example.com";
        int otp = 1234;

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setOtp(otp);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        ResponseEntity<String> response = userController.verification(email, otp);

        // Then
        verify(userRepository, times(1)).save(user);
        Assertions.assertTrue(user.getVerified());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("User verified successfully", response.getBody());
    }

    @Test
    public void testVerification_UserNotExists() {
        // Given
        String email = "test@example.com";
        int otp = 1234;

        when(userRepository.findByEmail(email)).thenReturn(null);

        // When
        ResponseEntity<String> response = userController.verification(email, otp);

        // Then
        verify(userRepository, never()).save(any(UserEntity.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testVerification_InvalidOTP() {
        // Given
        String email = "test@example.com";
        int otp = 1234;

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setOtp(5678);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        ResponseEntity<String> response = userController.verification(email, otp);

        // Then
        verify(userRepository, never()).save(any(UserEntity.class));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Error", response.getBody());
    }
    @Test
    public void testGetByUsername_UserExists() {
        // Given
        String username = "testUser";
        UserEntity user = new UserEntity();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        Optional<UserEntity> response = userController.getbyusername(username);

        // Then
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(user, response.get());
    }

    @Test
    public void testGetByUsername_UserNotExists() {
        // Given
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        Optional<UserEntity> response = userController.getbyusername(username);

        // Then
        Assertions.assertFalse(response.isPresent());
    }


   
}

