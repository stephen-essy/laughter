package com.laughter.laughter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laughter.laughter.Entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{}
