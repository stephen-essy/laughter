package com.laughter.laughter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laughter.laughter.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
