import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { StudentService } from '../../service/student-service';
import { Student } from '../../model/student.model';

@Component({
  selector: 'app-student-results',
  templateUrl: './student-result.component.html',
  standalone: false,
  styleUrls: ['./student-result.component.css']
})
export class StudentResultsComponent implements OnInit {
  all: Student[] = [];
  filtered: Student[] = [];
  tab: 'all' | 'pass' | 'fail' = 'all';
  loading = true;

  get passCount() { return this.all.filter(s => s.percentage >= 40).length; }
  get failCount()  { return this.all.filter(s => s.percentage < 40).length; }
  get avgPct()     {
    return this.all.length
      ? (this.all.reduce((a, b) => a + b.percentage, 0) / this.all.length).toFixed(1)
      : '0';
  }

  constructor(
    private studentService: StudentService,
    private router: Router,
    private cdr: ChangeDetectorRef   
  ) {}

  ngOnInit() {
    this.studentService.getAll().subscribe({
      next: (data) => {
        this.all = [...data].sort((a, b) => b.percentage - a.percentage);
        this.setTab('all');
        this.loading = false;
        this.cdr.detectChanges();   
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  setTab(tab: 'all' | 'pass' | 'fail') {
    this.tab = tab;
    if (tab === 'pass') this.filtered = this.all.filter(s => s.percentage >= 40);
    else if (tab === 'fail') this.filtered = this.all.filter(s => s.percentage < 40);
    else this.filtered = this.all;
  }

  grade(p: number): string {
    if (p >= 90) return 'A+';
    if (p >= 80) return 'A';
    if (p >= 70) return 'B';
    if (p >= 60) return 'C';
    if (p >= 40) return 'D';
    return 'F';
  }

  go(regNo: number) { this.router.navigate(['/students', regNo]); } 
}