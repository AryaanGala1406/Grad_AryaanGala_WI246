import { Component, OnInit } from '@angular/core';
import { StudentService } from '../services/student';
import { Student } from '../models/student';

@Component({
  selector: 'app-update-student',
  templateUrl: './update-student.html',
  standalone: false,
  styleUrls: ['./update-student.css']
})
export class UpdateStudent implements OnInit {

  students: Student[] = [];
  selectedRegNo!: number;

  student!: Student;

  message: string = "";

  constructor(private studentService: StudentService) {}

  ngOnInit() {
    this.students = this.studentService.getStudents();
  }

  loadStudent() {
    let s = this.studentService.getStudentByRegNo(this.selectedRegNo);
    if (s) {
      this.student = { ...s }; // clone object
    }
  }

  update(event: any) {
    event.preventDefault();

    this.studentService.updateStudent(this.student);

    this.message = "Student Updated Successfully";
  }

}