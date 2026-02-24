package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Student;
import com.example.demo.repo.StudentRepo;

@RestController
public class StudentController {
	
	@Autowired
	StudentRepo repo;
	
	// GET/students - Get all the students
	@GetMapping("/students")
	public List<Student> getStudents() {
		return repo.findAll();
	}
	
	// GET/students/regNo - Specific student details of the given Registration number
	@GetMapping("/students/{regNo}")
	public Optional<Student> getStudentByRegNo(@PathVariable Integer regNo) {
		return repo.findById(regNo);
	}
	
	// POST/students - Insert a student record
	@PostMapping("/students")
	public String insertStudent(@RequestBody Student s) {
		if(repo.existsById(s.getRegNo())) 
			return "Sorry! Student already exists";
		repo.save(s);
		return "Successfully added Student record!";
	}
	
	// PUT/students/regNo - Update specific student record
	@PutMapping("/students/{regNo}")
	public String updateStudent(@PathVariable Integer regNo, @RequestBody Student s) {
		if(s.getRegNo() != regNo) 
			return "Student Ids does not match";
		if(!repo.existsById(regNo)) 
			return "Sorry! Student with given ID does not exist";
		repo.save(s);
		return "Successfully updated employee record";
	}
	
	// PATCH /students/{regNo} - Partial Update
	@PatchMapping("/students/{regNo}")
	public String updatePartialStudent(@PathVariable Integer regNo, @RequestBody Student s) {
	    Optional<Student> optionalStudent = repo.findById(regNo);
	    System.out.println(optionalStudent);
	    if (optionalStudent.isEmpty()) {
	        return "Student not found";
	    }
	    Student existingStudent = optionalStudent.get();
	    if (s.getRollNo() != null)
	        existingStudent.setRollNo(s.getRollNo());
	    if (s.getName() != null)
	        existingStudent.setName(s.getName());
	    if (s.getStandard() != null)
	        existingStudent.setStandard(s.getStandard());
	    if (s.getSchool() != null)
	        existingStudent.setSchool(s.getSchool());
	    if (s.getGender() != null)
	        existingStudent.setGender(s.getGender());
	    if (s.getPrecentage() != null)
	        existingStudent.setPrecentage(s.getPrecentage());
	    repo.save(existingStudent);
	    return "Partially updated successfully";
	}

	// DELETE/students/regNo - Remove the student record for the given Registration number
	@DeleteMapping("/students/{regNo}")
	public String removeStudent(@PathVariable Integer regNo) {
		if(!repo.existsById(regNo))
			return "No Record available with the given ID";
		else {
			repo.deleteById(regNo);
		return "Student record deleted Successfully";
		}
	}
	
	// GET/students/school?name=KV - List all students belonging to that school
	@GetMapping("/students/school")
	public List<Student> getStudentsBySchool(@RequestParam("name") String school) {
	    return repo.findBySchool(school);
	}
	
	// GET/students/school/count?name=DPS - Total strength in that school
	@GetMapping("/students/school/count")
	public long getNoOfStudentsBySchool(@RequestParam("name") String school) {
	    return repo.countBySchool(school);
	}
	
	// GET/students/school/standard/count?class=5 - Total number of students in 5th standard
	@GetMapping("/students/standard/count")
	public long getNoOfStudentsInStandard(@RequestParam("class") int standard) {
	    return repo.countByStandard(standard);
	}
	
	@GetMapping("/students/school/standard/count")
	public long getNoOfStudentsBySchool(@RequestParam("name") String school, @RequestParam("class") int standard) {
	    return repo.countBySchoolAndStandard(school, standard);
	}
	
	// GET/students/result?pass=true/false - List the students in descending order of their percentage (40% and above is pass)
	@GetMapping("/students/result")
	public List<Student> getStudentByResult(@RequestParam("pass") boolean pass) {
		if(pass) {
	        return repo.findByPrecentageGreaterThanEqualOrderByPrecentageDesc(40);
	    } else {
	        return repo.findByPrecentageLessThanOrderByPrecentageDesc(40);
	    }
	}
	
	// GET/students/strength?gender=MALE&class=5 - How many Male students in standard 5
	@GetMapping("/students/strength")
	public long getNoOfStudentsByGenderAndStandard(@RequestParam("gender") String gender, @RequestParam("class") int standard) {
	    return repo.countByGenderAndStandard(gender, standard);
	}

}
