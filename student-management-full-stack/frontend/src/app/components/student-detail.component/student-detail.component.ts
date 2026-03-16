import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../service/student-service';
import { ToastService } from '../../service/toast-service';
import { Student } from '../../model/student.model';

@Component({
  selector: 'app-student-detail',
  templateUrl: './student-detail.component.html',
  standalone: false,
  styleUrls: ['./student-detail.component.css']
})
export class StudentDetailComponent implements OnInit {
  student: Student | null = null;
  loading = true;
  showConfirm = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private studentService: StudentService,
    private toastService: ToastService,
    private cdr: ChangeDetectorRef  
  ) {}

  ngOnInit() {
    const regNo = +this.route.snapshot.params['regNo']; 
    this.studentService.getOne(regNo).subscribe({
      next: (s) => {
        this.student = s;
        this.loading = false;
        this.cdr.detectChanges();   
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
        this.router.navigate(['/students']);
      }
    });
  }

  edit() {
    this.router.navigate(['/students', this.student!.regNo, 'edit']);
  }

  confirmDelete() { this.showConfirm = true; }

  doDelete() {
    this.studentService.delete(this.student!.regNo).subscribe({
      next: () => {
        this.toastService.success('Student deleted');
        this.router.navigate(['/students']);
      }
    });
  }

  getGrade(p: number): string {
    if (p >= 90) return 'A+';
    if (p >= 80) return 'A';
    if (p >= 70) return 'B';
    if (p >= 60) return 'C';
    if (p >= 40) return 'D';
    return 'F';
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase();
  }
}