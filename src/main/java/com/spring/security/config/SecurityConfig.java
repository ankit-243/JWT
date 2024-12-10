package com.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/user/create","/user/login")
				.permitAll()
				.anyRequest().authenticated());// it is going to authenticate
																						// all the requests

		http.formLogin(Customizer.withDefaults()); // enabling the form login
		http.httpBasic(Customizer.withDefaults()); // enabling the basic auth
		http.csrf(csrf -> csrf.disable()); // disable the csrf
		// http.authorizeHttpRequests(request -> request.requestMatchers(null))

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // here we
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);			 																						// are
																											// managing
																											// our
																											// sessions
		return http.build();

	}
    
	//FOr static
	/*
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User.withDefaultPasswordEncoder().username("Ankit").password("ankit").roles("admin")
				.build();
		
		UserDetails user2 = User.withDefaultPasswordEncoder().username("Raka").password("raka").roles("admin")
				.build();

		return new InMemoryUserDetailsManager(user1,user2);
	}
	
	*/


	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); //helps us to connect with db for creds
		
		provider.setUserDetailsService(userDetailsService);
		//provider.setPasswordEncoder(passwordEncoder);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
