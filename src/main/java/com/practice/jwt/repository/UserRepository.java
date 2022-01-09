package com.practice.jwt.repository;

import com.practice.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository< User , Long> {

    public User findByUsername (String username);
}
