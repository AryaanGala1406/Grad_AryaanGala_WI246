package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {
	@Id 
	private Integer regNo;
	private Integer rollNo;
	private String name;
	private Integer standard;
	private String school;
	private String gender;
	private Integer precentage;
	
	
	public Student() {
	}

	public Student(Integer regNo, Integer rollNo, String name, Integer standard, String school, String gender,
			Integer precentage) {
		this.regNo = regNo;
		this.rollNo = rollNo;
		this.name = name;
		this.standard = standard;
		this.school = school;
		this.gender = gender;
		this.precentage = precentage;
	}

	public Integer getRegNo() {
	    return regNo;
	}

	public void setRegNo(Integer regNo) {
	    this.regNo = regNo;
	}
	public Integer getRollNo() {
		return rollNo;
	}
	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStandard() {
		return standard;
	}
	public void setStandard(Integer standard) {
		this.standard = standard;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getPrecentage() {
		return precentage;
	}
	public void setPrecentage(Integer precentage) {
		this.precentage = precentage;
	}
	
}
