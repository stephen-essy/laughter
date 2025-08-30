package com.laughter.laughter.Controller;

import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.laughter.laughter.Security.AESEncryption;
import com.laughter.laughter.Security.JwtUtil;
import com.laughter.laughter.Security.RecoveryStringGenerator;
import com.laughter.laughter.Service.EmailServices;
import com.laughter.laughter.Service.UserDetailsServiceImplementation;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("laughter/user")
@CrossOrigin(origins = "http://127.0.0.1:5500/", maxAge = 3600)
@Getter
@Setter
@RequiredArgsConstructor
public class UserController {
    
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServices emailServices;
    @Value("${app.secret.value}")
    private String secretString;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImplementation userDetailsImplementation;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Email Exists in our system", null));
            }
            String name = userDTO.getName();
            String email = userDTO.getEmail();
            SecretKey secretKey = AESEncryption.getSecretKey(secretString);
            String password = AESEncryption.encrypt(userDTO.getPassword(), secretKey);
            String recoverString = RecoveryStringGenerator.generateRecoveryString(name, email, password);
            String securedRecoveryString = new BCryptPasswordEncoder().encode(recoverString);
            User user = new User(name, email, password, securedRecoveryString);
            userRepository.save(user);
            try {
                emailServices.sendAccountCreationVerification(email, userDTO, recoverString);
                System.out.println("Email sent to Recipient :" + email);
            } catch (Exception e) {
                System.out.println("Failed to send Email to :" + email + " Accompaning problems are " + e.getMessage());
            }
            return ResponseEntity.ok(new ApiResponse(true, "account is created Successful,Check your email", null));
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error in processing", null));
        }
    }

    @PostMapping("/recover")
    public ResponseEntity<AuthResponse> recoverPassword(@Valid @RequestBody UserDTO userDTO) {
        try {
            Optional<User> userFound = userRepository.findByEmail(userDTO.getEmail());
            if (userFound.isEmpty()) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Email does not exist"));
            }
            User user = userFound.get();
            if (!passwordEncoder.matches(userDTO.getRecoveryString(), user.getRecoveryString())) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Incorrect Code entered"));
            }
            String recoverPassword = AESEncryption.decrypt(user.getPassword(),
                    AESEncryption.getSecretKey(secretString));
            try {
                emailServices.sendRecoveryPassword(userDTO.getEmail(), user, recoverPassword);
                System.out.println("Email sent to lazy recipient :" + user.getEmail());
            } catch (Exception e) {
                System.out.println("Error :" + e.getMessage());
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Error in Sending your password !"));
            }
            return ResponseEntity.ok(new AuthResponse(false, "Password is sent to your email !"));
        } catch (Exception e) {
            System.out.println("ERROR :" + e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Error in processing your request !"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserDTO userDTO) throws Exception {
        try {
            Optional<User> userFound = userRepository.findByEmail(userDTO.getEmail());

            if (userFound.isEmpty()) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Incorrect Email"));
            }
            User user = userFound.get();
            String rawPassword=AESEncryption.decrypt(user.getPassword(),AESEncryption.getSecretKey(secretString));
            if (rawPassword.equals(userDTO.getPassword())){
                // Jwt implementation to provide token and send to fontend
                UserDetails userDetails = userDetailsImplementation.loadUserByUsername(userDTO.getEmail());
                String token = jwtUtils.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(
                        true,
                        "Successful Authenticated !",
                        token,
                        user.getName()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new AuthResponse(
                                false,
                                "Incorrect Password Entered !"));
            }

        } catch (UsernameNotFoundException e) {
            System.err.println("Error :" + e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Error in processing"));
        }
    }


    @MessageMapping("/greet")
    @SendTo("/topic/greetings")
    public String  greet(String name){
        return "Hello,"+ name +" !";
    }
}
