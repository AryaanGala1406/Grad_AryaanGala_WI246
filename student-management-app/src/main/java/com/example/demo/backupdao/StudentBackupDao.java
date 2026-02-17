package com.example.demo.backupdao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Student;

public interface StudentBackupDao extends JpaRepository<Student, Integer> {
}
