import { Injectable } from '@angular/core';
import { Student } from '../models/student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  students: Student[] = [];

  constructor() { }

  addStudent(student: Student) {
    this.students.push(student);
  }

  getStudents() {
    return this.students;
  }

  deleteStudent(regNo: number) {
    this.students = this.students.filter(s => s.regNo != regNo);
  }

  updateStudent(updatedStudent: Student) {
    for (let i = 0; i < this.students.length; i++) {
      if (this.students[i].regNo == updatedStudent.regNo) {
        this.students[i] = updatedStudent;
        break;
      }
    }
  }

  getStudentByRegNo(regNo: number) {
    return this.students.find(s => s.regNo == regNo);
  }
}