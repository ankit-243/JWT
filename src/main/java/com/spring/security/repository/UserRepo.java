package com.spring.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}
