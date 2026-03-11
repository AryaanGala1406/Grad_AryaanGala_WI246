import { Component, OnInit } from '@angular/core';
import { StudentService } from '../services/student';
import { Student } from '../models/student';

@Component({
  selector: 'app-remove-student',
  templateUrl: './remove-student.html',
  standalone: false,
  styleUrls: ['./remove-student.css']
})
export class RemoveStudent implements OnInit {

  students: Student[] = [];
  selectedRegNo!: number;

  student!: Student;

  message: string = "";

  constructor(private studentService: StudentService) { }

  ngOnInit() {
    this.students = this.studentService.getStudents();
  }

  loadStudent() {
    let s = this.studentService.getStudentByRegNo(this.selectedRegNo);
    if (s) {
      this.student = s;
    }
  }

  remove() {
    const confirmDelete = confirm("Are you sure you want to delete this student?");

    if (confirmDelete) {
      this.studentService.deleteStudent(this.selectedRegNo);
      this.message = "Student Removed Successfully";
      this.students = this.studentService.getStudents();
      this.student = undefined as any;
    } else {
      // Do nothing if user cancels
      this.message = "Deletion cancelled";
    }
  }

}