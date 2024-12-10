package com.spring.security.config;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.security.service.JwtService;
import com.spring.security.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtService jwtService;
	
	@Autowired 
	ApplicationContext applicationContext;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token =null;
		String username=null;
		
		//we have to check whether use sent the token or not and if user sends the token what token it is 
		if(authHeader != null && authHeader.startsWith("Bearer")) {
			token = authHeader.substring(7);
			//we will be extracting the username from the token itself
			username = jwtService.extractUsername(token);
		}
		
		//once i  get the username and token lets validate the token
		//we also want to check if the username is null or not and we also want to check if the authentication is already done and theere is a token already in security context for the particular request
		if(username !=null && SecurityContextHolder.getContext().getAuthentication() ==null) {
			UserDetails userDetails =  (UserDetails) applicationContext.getBean(MyUserDetailsService.class);
			if(jwtService.validateToken(token,userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				//we will be setting the token in security context too because use UsernamePasswordAuthenticationToken does not hold the data for the token
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//with this we have set the token in security context holder too
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			
		}
		
		//to continuing the filter chain
		filterChain.doFilter(request, response);
		
	}

}
