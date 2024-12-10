package com.spring.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.entity.Students;
import com.spring.security.repository.StudentsRepo;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CSRFTestController {
	@Autowired
	StudentsRepo repo;

	@GetMapping("/getstudents")
	public List<Students> getStudents() {
		return repo.findAll();
	}

	@PostMapping("/addstudents")
	public ResponseEntity<?> addStudents(@RequestBody Students student) {
		repo.save(student);
		return new ResponseEntity<>("Successfully added", HttpStatus.CREATED);
	}

	@GetMapping("/gettoken")
	public CsrfToken getToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
}
