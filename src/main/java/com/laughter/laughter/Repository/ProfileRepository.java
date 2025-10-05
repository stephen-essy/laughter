package com.laughter.laughter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.laughter.laughter.Entity.Profile;
import com.laughter.laughter.Entity.User;


public interface ProfileRepository extends JpaRepository<Profile, Long>{
    Optional <Profile> findByUser(User user);
}
