package com.spring.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.security.entity.User;
import com.spring.security.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepo userRepo;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		  User user = userRepo.findByUsername(username).get();
		  
		return new UserDetailsImpl(user);
	}

}
