package dpc.fr.back.controller;

import dpc.fr.back.dto.AuthResponseDTO;
import dpc.fr.back.dto.LoginDto;
import dpc.fr.back.dto.RegisterDto;
import dpc.fr.back.entity.Role;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.RoleRepository;
import dpc.fr.back.repository.UserRepository;
import dpc.fr.back.security.JWTGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    private final RoleRepository roleRepository;


    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);

        if (user != null && user.getVerified()) { // Check if the user account is verified
            String fullname = user.getFullName();
            AuthResponseDTO response = new AuthResponseDTO(fullname, token);
            return ResponseEntity.ok(response);
        } else {
            // User account is not verified
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setFullName(registerDto.getFullName());
        user.setAddress(registerDto.getAddress());
        user.setEmail(registerDto.getEmail());
        user.setDeliveredOn(registerDto.getDeliveredOn());
        user.setNumber(registerDto.getNumber());
        user.setDriverLicense(registerDto.getDriverLicense());
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        userRepository.save(user);
        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
}
