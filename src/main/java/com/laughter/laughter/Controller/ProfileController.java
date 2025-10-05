package com.laughter.laughter.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laughter.laughter.DTO.ProfileDTO;
import com.laughter.laughter.Entity.Profile;
import com.laughter.laughter.Entity.User;
import com.laughter.laughter.Repository.ProfileRepository;
import com.laughter.laughter.Repository.UserRepository;
import com.laughter.laughter.Responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("laughter/profile")
@CrossOrigin(origins = "http://127.0.0.1:5500/", maxAge = 3600)
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRespository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProfile(@Valid @RequestBody ProfileDTO profileDTO,@AuthenticationPrincipal UserDetails userDetails){
        String email=userDetails.getUsername();
        Optional<User> user=userRepository.findByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,"Unauthorized Access",null));
        }

        User userFound =user.get();
        Profile profile = new Profile(
            userFound,
            profileDTO.getGender(),
            profileDTO.getPhoneNumber(),
            profileDTO.getUniversity(),
            profileDTO.getCorse()
        );   
        profileRespository.save(profile);
        return ResponseEntity.ok(new ApiResponse(true,"Profile added successful",null));
    }

}
