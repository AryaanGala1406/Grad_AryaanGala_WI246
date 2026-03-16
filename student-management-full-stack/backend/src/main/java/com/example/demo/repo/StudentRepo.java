package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Student;

public interface StudentRepo extends JpaRepository<Student, Integer>{
	public List<Student> findBySchool(String school);
	long countBySchool(String school);
	long countBySchoolAndStandard(String school, int standard);
	long countByStandard(int standard);
	long countByGenderAndStandard(String gender, int standard);
	List<Student> findByPercentageGreaterThanEqualOrderByPercentageDesc(int percentage);
	List<Student> findByPercentageLessThanOrderByPercentageDesc(int percentage);
}


