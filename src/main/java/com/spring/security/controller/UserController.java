package com.spring.security.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.entity.User;
import com.spring.security.repository.UserRepo;
import com.spring.security.service.JwtService;

import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.InvalidKeyException;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepo userrepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;

	@PostMapping("create")
	public void createUser(@RequestBody User user) {

		user.setPassword(encoder.encode(user.getPassword()));

		userrepo.save(user);
		System.out.println(user);

	}

	@PostMapping("login")
	public String login(@RequestBody User user) throws InvalidKeyException, DecodingException, NoSuchAlgorithmException {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		
		if (authentication.isAuthenticated())
			return jwtService.generateToken(user.getUsername());
		else
			return "Failure";
		

	}
//we have UsernamePasswordAuthenticationToken which authenticate the username and password and then return the Authentication object.

}
