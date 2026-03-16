import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { StudentService } from '../../service/student-service';
import { ToastService } from '../../service/toast-service';
import { Student } from '../../model/student.model';

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.css'],
  standalone: false,
})
export class StudentListComponent implements OnInit {
  students: Student[] = [];
  filtered: Student[] = [];
  loading = true;

  search = '';
  filterGender = '';
  filterStandard = '';

  showConfirm = false;
  deleteTarget: Student | null = null;

  constructor(
    private studentService: StudentService,
    private toastService: ToastService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.search = params['q'] || '';
    });
    this.load();
  }

  load() {
    this.loading = true;
    this.studentService.getAll().subscribe({
      next: (data) => {
        this.students = data;
        this.applyFilter();
        this.loading = false;
        this.cdr.detectChanges();   
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();   
      }
    });
  }

  applyFilter() {
    let result = [...this.students];
    if (this.search) {
      const q = this.search.toLowerCase();
      result = result.filter(s =>
        s.name.toLowerCase().includes(q) ||
        s.regNo.toString().includes(q)
      );
    }
    if (this.filterGender) {
      result = result.filter(s => s.gender === this.filterGender);
    }
    if (this.filterStandard) {
      result = result.filter(s => s.standard === +this.filterStandard);
    }
    this.filtered = result;
  }

  goToDetail(regNo: number) {          
    this.router.navigate(['/students', regNo]);
  }

  goToEdit(regNo: number, e: Event) {   
    e.stopPropagation();
    this.router.navigate(['/students', regNo, 'edit']);
  }

  askDelete(student: Student, e: Event) {
    e.stopPropagation();
    this.deleteTarget = student;
    this.showConfirm = true;
  }

  confirmDelete() {
    if (!this.deleteTarget) return;
    this.studentService.delete(this.deleteTarget.regNo).subscribe({
      next: () => {
        this.toastService.success('Student deleted successfully');
        this.showConfirm = false;
        this.deleteTarget = null;
        this.load();
      }
    });
  }

  cancelDelete() {
    this.showConfirm = false;
    this.deleteTarget = null;
  }

  getPassClass(pct: number): string {
    if (pct >= 80) return 'grade-a';
    if (pct >= 60) return 'grade-b';
    if (pct >= 40) return 'grade-c';
    return 'grade-f';
  }
}