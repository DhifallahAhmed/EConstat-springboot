package dpc.fr.back.controller;

import dpc.fr.back.dto.RegisterDto;
import dpc.fr.back.entity.ChangePasswordRequest;
import dpc.fr.back.entity.Role;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.RoleRepository;
import dpc.fr.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @PostMapping("add")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername()))
        {
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

        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {

        UserEntity user = userRepository.findById(userId).orElse(null);
        if(user != null) {

            userRepository.delete(user);

            return ResponseEntity.ok("User deleted successfully");
        }
        if (user == null) {
            return ResponseEntity.ok("User Not Found");
        }
        else {
            return ResponseEntity.internalServerError().body("Error");
        }

    }
    @GetMapping("get")
    public List<UserEntity> findAllcars() {
        List<UserEntity> users = userRepository.findAll();
        return users;
    }
    @GetMapping("get/{id}")
    public UserEntity findAllcars(int id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return user;
    }
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody UserEntity updatedUser) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFullName(updatedUser.getFullName());
            user.setAddress(updatedUser.getAddress());
            user.setNumber(updatedUser.getNumber());
            user.setDriverLicense(updatedUser.getDriverLicense());
            user.setDeliveredOn(updatedUser.getDeliveredOn());
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully");
        }
        if (user == null){
            return ResponseEntity.ok("User Not Found");
        }
        else return ResponseEntity.internalServerError().body("Error");
    }
    @PutMapping("/users/{email}/password")
    public ResponseEntity<String> changePassword(@PathVariable String email, @RequestBody ChangePasswordRequest request) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Verify the user's current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid current password");
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }
}
