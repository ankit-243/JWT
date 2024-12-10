package com.spring.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
    @GetMapping("/name")
    public String name() {
    	return "Ankit";
    }
}
