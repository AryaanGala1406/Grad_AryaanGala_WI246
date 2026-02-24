package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Employee;
import com.example.demo.repo.EmpRepo;

@RestController
public class EmpController {

	@Autowired
	EmpRepo repo;

//	@GetMapping("/employees/{id}")
//	public Employee getEmployee(@PathVariable int id) {
//	    return repo.findById(id)
//	            .orElseThrow(() -> new RuntimeException("Employee not found"));
//	}

//	@GetMapping("/employees/{id}")
//	public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
//	    return repo.findById(id)
//	            .map(ResponseEntity::ok)
//	            .orElse(ResponseEntity.noContent().build());
//	}

	@GetMapping("/employee/{id}")
	public ResponseEntity<Employee> insertEmployee(@PathVariable int id) {
		Optional<Employee> emp = repo.findById(id);
		if (emp.isPresent())
			return ResponseEntity.ok(emp.get());
		else
			return ResponseEntity.status(204).build();
	}

}
