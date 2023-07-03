package dpc.fr.back.controller;

import dpc.fr.back.dto.RegisterDto;
import dpc.fr.back.entity.ChangePasswordRequest;
import dpc.fr.back.entity.Role;
import dpc.fr.back.entity.UserEntity;
import dpc.fr.back.repository.RoleRepository;
import dpc.fr.back.repository.UserRepository;
import dpc.fr.back.service.EmailSenderService;
import dpc.fr.back.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private EmailSenderService senderService;
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

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }
    @PutMapping("/{email}/forgot")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        UserEntity user = userRepository.findByEmail(email);
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        senderService.sendSimpleEmail(email,"Reset password","Your new password is "+ password.toString()+" you can change it anytime you want!");
        return ResponseEntity.ok("Password changed successfully");
    }




    @PostMapping("/verif/{email}")
    public ResponseEntity<String> verification(@PathVariable String email,@RequestBody int otp)
    {
        System.out.println(email);
       // int min = 1000;  // Minimum OTP value
     //   int max = 9999;  // Maximum OTP value
      //  Random random = new Random();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (user != null && otp==user.getOtp()) {
            user.setVerified(Boolean.TRUE);
            userRepository.save(user);
            return ResponseEntity.ok("User verified successfully");
        }
        else return ResponseEntity.internalServerError().body("Error");
    }
    @PostMapping ("sendotp/{email}")
    public ResponseEntity<String> sendotp(@PathVariable String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        int min = 1000;  // Minimum OTP value
        int max = 9999;  // Maximum OTP value
        Random random = new Random();
        int sentotp = random.nextInt(max - min + 1) + min;
        user.setOtp(sentotp);
        userRepository.save(user);
        senderService.sendSimpleEmail(email,"OTP","Your OTP is "+ sentotp +" you can verify your account by typing it in the link below");
        return ResponseEntity.ok("OTP sent successfully");
    }

}
