package dpc.fr.back.api.controller;

import dpc.fr.back.controller.AuthController;
import dpc.fr.back.dto.AuthResponseDTO;
import dpc.fr.back.dto.LoginDto;
import dpc.fr.back.dto.RegisterDto;
import dpc.fr.back.entity.Role;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.RoleRepository;
import dpc.fr.back.repository.UserRepository;
import dpc.fr.back.security.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTGenerator jwtGenerator;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        // Given
        String username = "test_user";
        String password = "test_password";
        String fullName = "Test User";
        String token = "generated_token";

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setVerified(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        when(jwtGenerator.generateToken(authentication)).thenReturn(token);

        // When
        ResponseEntity<AuthResponseDTO> responseEntity = authController.login(loginDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponseDTO authResponseDTO = responseEntity.getBody();
        assertThat(authResponseDTO).isNotNull();
        assertThat(authResponseDTO.getFullname()).isEqualTo(fullName);
        assertThat(authResponseDTO.getAccessToken()).isEqualTo(token);
    }

    @Test
    public void testLogin_Unauthorized() {
        // Given
        String username = "test_user";
        String password = "test_password";

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setVerified(false); // User account is not verified
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<AuthResponseDTO> responseEntity = authController.login(loginDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }
    @Test
    public void testRegister_Success() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("test_user");
        // Set other properties for registerDto

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        // Set other properties for user

        Role role = new Role();
        role.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any())).thenReturn(user);

        // When
        ResponseEntity<String> responseEntity = authController.register(registerDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("User registered success!");
    }

    @Test
    public void testRegister_Failure_UsernameTaken() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("test_user");
        // Set other properties for registerDto

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

        // When
        ResponseEntity<String> responseEntity = authController.register(registerDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Username is taken!");
    }
}