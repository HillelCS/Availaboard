package com.availaboard.engine.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.availaboard.engine.resource.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	@Autowired
	Optional<User> findUserByUsername(String username);
}