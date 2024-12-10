package com.spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.entity.Students;

public interface StudentsRepo extends JpaRepository<Students,Integer> {

}
