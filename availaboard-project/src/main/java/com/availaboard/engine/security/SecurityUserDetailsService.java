package com.availaboard.engine.security;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.availaboard.engine.resource.User;
import com.availaboard.engine.security.UserRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username)
				.orElseGet((Supplier<? extends User>) new UsernameNotFoundException("User not present"));
		return user;
	}

	public void createUser(UserDetails user) {
		userRepository.save((User) user);
	}
}