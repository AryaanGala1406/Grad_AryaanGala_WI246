import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { StudentService } from '../../service/student-service';
import { Student } from '../../model/student.model';

@Component({
  selector: 'app-student-analytics',
  standalone: false,
  templateUrl: './student-analytics.component.html',
  styleUrls: ['./student-analytics.component.css']
})
export class StudentAnalyticsComponent implements OnInit {
  students: Student[] = [];
  loading = true;

  genderStats: { label: string; count: number; pct: number; color: string }[] = [];
  schoolStats: { name: string; count: number }[] = [];
  standardStats: { standard: number; count: number }[] = [];
  maxStd = 1;

  schoolQuery = ''; schoolResult: number | null = null;
  stdQuery = ''; stdResult: number | null = null;
  sgGender = ''; sgStd = ''; sgResult: number | null = null;

  constructor(
    private studentService: StudentService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.studentService.getAll().subscribe({
      next: (data) => {
        this.students = data;
        this.buildStats(data);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  buildStats(data: Student[]) {
    const total = data.length || 1;

    // Gender
    const gMap: Record<string, number> = {};
    data.forEach(s => gMap[s.gender] = (gMap[s.gender] || 0) + 1);
    const colors: Record<string, string> = { MALE: '#3b82f6', FEMALE: '#ec4899', OTHER: '#8b5cf6' };
    this.genderStats = Object.entries(gMap).map(([label, count]) => ({
      label, count, pct: Math.round((count / total) * 100), color: colors[label]
    }));

    // Schools
    const sMap: Record<string, number> = {};
    data.forEach(s => sMap[s.school] = (sMap[s.school] || 0) + 1);
    this.schoolStats = Object.entries(sMap)
      .map(([name, count]) => ({ name, count }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 8);

    // Standards
    const stMap: Record<number, number> = {};
    data.forEach(s => stMap[s.standard] = (stMap[s.standard] || 0) + 1);
    this.standardStats = Object.entries(stMap)
      .map(([standard, count]) => ({ standard: +standard, count }))
      .sort((a, b) => a.standard - b.standard);
    this.maxStd = Math.max(...this.standardStats.map(s => s.count), 1);
  }

  querySchool() {
    if (!this.schoolQuery.trim()) return;
    this.studentService.getSchoolCount(this.schoolQuery)
      .subscribe({
        next: r => {
          this.schoolResult = r.count;
          this.cdr.detectChanges();
        }
      });
  }

  queryStandard() {
    if (!this.stdQuery) return;
    this.studentService.getStandardCount(+this.stdQuery)
      .subscribe({
        next: r => {
          this.stdResult = r.count;
          this.cdr.detectChanges();
        }
      });
  }

  queryStrength() {
    if (!this.sgGender || !this.sgStd) return;
    this.studentService.getStrength(this.sgGender, +this.sgStd)
      .subscribe({
        next: r => {
          this.sgResult = r.count;
          this.cdr.detectChanges();
        }
      });
  }
}

