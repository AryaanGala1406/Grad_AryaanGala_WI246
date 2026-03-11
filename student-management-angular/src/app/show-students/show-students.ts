import { Component, OnInit } from '@angular/core';
import { StudentService } from '../services/student';
import { Student } from '../models/student';
import { User } from '../services/user';

@Component({
  selector: 'app-show-students',
  standalone: false,
  templateUrl: './show-students.html',
  styleUrl: './show-students.css',
})

export class ShowStudents implements OnInit {

  students: Student[] = [];

  constructor(private studentService: StudentService, public user: User) {}

  ngOnInit(): void {
    this.show();   // Automatically called when component loads
  }
  show() {
    this.students = this.studentService.getStudents();
  }
}
