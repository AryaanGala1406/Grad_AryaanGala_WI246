package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.backupdao.StudentBackupDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.entities.Student;

@Controller
public class StudentWebController {
	
	@Autowired
	StudentDao primaryDao;

	@Autowired
	StudentBackupDao backupDao;

	@RequestMapping("/")
	public String empManagement() {
		return "index.jsp";
	}
	
	@RequestMapping("/addStudent")
	public String addEmployee(Student s) {
		if(backupDao.existsById(s.getRollNo()))
			System.out.println("Student already exists");
		else {
		        primaryDao.save(s);   // H2
		        backupDao.save(s);    // PostgreSQL backup
		    }
		return "index.jsp";
	}
}
