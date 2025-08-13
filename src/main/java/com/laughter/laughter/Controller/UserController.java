package com.laughter.laughter.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laughter.laughter.DTO.UserDTO;
import com.laughter.laughter.Entity.User;
import com.laughter.laughter.Repository.UserRepository;
import com.laughter.laughter.Responses.ApiResponse;
import com.laughter.laughter.Responses.AuthResponse;
import com.laughter.laughter.Security.RecoveryStringGenerator;
import com.laughter.laughter.Service.EmailServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/laughter/user")
@CrossOrigin(origins = "", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServices emailServices;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Email Exists in our system", null));
            }
            String name = userDTO.getName();
            String email = userDTO.getEmail();
            String password = new BCryptPasswordEncoder().encode(userDTO.getPassword());
            String recoverString=RecoveryStringGenerator.generateRecoveryString(name, email, password);
            User user = new User(name, email, password);
            userRepository.save(user);
            try {
                emailServices.sendAccountCreationVerification(email, userDTO,recoverString);
                System.out.print("Email sent to Recipient :" + email);
            } catch (Exception e) {
                System.out.println("Failed to send Email to :" + email + " Accompaning problems are " + e.getMessage());
            }
            return ResponseEntity.ok(new ApiResponse(true, "Successfully", null));
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error in processing", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserDTO userDTO) {
        try {
            Optional<User> userFound = userRepository.findByEmail(userDTO.getEmail());

            if (userFound.isEmpty()) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Incorrect Email"));
            }
            User user = userFound.get();
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {

                // Jwt implementation to provide token and send to backend
                return ResponseEntity.ok(new AuthResponse(
                        true,
                        "Successful Authenticated !"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new AuthResponse(
                                false,
                                "Incorrect Password Entered !"));
            }

        } catch (Exception e) {
            System.err.println("Error :" + e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Error in processing"));
        }
    }

}
