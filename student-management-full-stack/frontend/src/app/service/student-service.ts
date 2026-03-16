import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Student, StudentCreate, StudentUpdate } from '../model/student.model';

@Injectable({
  providedIn: 'root',
})
export class StudentService {

  private BASE_URL = 'http://localhost:8080'; 

  constructor(private http: HttpClient) {}

  // GET /students
  getAll(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.BASE_URL}/students`);
  }

  // GET /students/:regNo
  getOne(regNo: number): Observable<Student> {
    return this.http.get<Student>(`${this.BASE_URL}/students/${regNo}`);
  }

  // POST /students
  create(data: StudentCreate): Observable<Student> {
    return this.http.post<Student>(`${this.BASE_URL}/students`, data);
  }

  // PUT /students/:regNo
  update(regNo: number, data: StudentCreate): Observable<Student> {
    return this.http.put<Student>(`${this.BASE_URL}/students/${regNo}`, data);
  }

  // PATCH /students/:regNo
  patch(regNo: number, data: StudentUpdate): Observable<Student> {
    return this.http.patch<Student>(`${this.BASE_URL}/students/${regNo}`, data);
  }

  // DELETE /students/:regNo
  delete(regNo: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/students/${regNo}`);
  }

  // GET /students/school?name=KV
  getBySchool(name: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.BASE_URL}/students/school`, {
      params: new HttpParams().set('name', name)
    });
  }

  // GET /students/school/count?name=DPS
  getSchoolCount(name: string): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.BASE_URL}/students/school/count`, {
      params: new HttpParams().set('name', name)
    });
  }

  // GET /students/school/standard/count?class=5
  getStandardCount(standard: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.BASE_URL}/students/school/standard/count`, {
      params: new HttpParams().set('class', standard.toString())
    });
  }

  // GET /students/result?pass=true/false
  getByResult(pass: boolean): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.BASE_URL}/students/result`, {
      params: new HttpParams().set('pass', pass.toString())
    });
  }

  // GET /students/strength?gender=MALE&standard=5
  getStrength(gender: string, standard: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.BASE_URL}/students/strength`, {
      params: new HttpParams()
        .set('gender', gender)
        .set('standard', standard.toString())
    });
  }
}
