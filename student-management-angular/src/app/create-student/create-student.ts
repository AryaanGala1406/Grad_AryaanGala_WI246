import { Component } from '@angular/core';
import { StudentService } from '../services/student';
import { Student } from '../models/student';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-student',
  templateUrl: './create-student.html',
  standalone: false,
  styleUrls: ['./create-student.css']
})
export class CreateStudent {

  constructor(
    private studentService: StudentService,
    private router: Router
  ) {}

  create(event: any) {
    event.preventDefault();

    let student: Student = {
      regNo: event.target.elements[0].value,
      rollNo: Number(event.target.elements[1].value),
      name: event.target.elements[2].value,
      standard: event.target.elements[3].value,
      school: event.target.elements[4].value
    };

    this.studentService.addStudent(student);

    // success prompt
    alert("Student created successfully!");

    // redirect to home page
    this.router.navigate(['/admin-menu']);
  }
}