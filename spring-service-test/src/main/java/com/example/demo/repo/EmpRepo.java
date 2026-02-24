package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Employee;

public interface EmpRepo extends JpaRepository<Employee, Integer>{
	
}


