import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { StudentService } from '../../service/student-service';
import { ToastService } from '../../service/toast-service';

@Component({
  selector: 'app-student-form',
  templateUrl: './student-form.component.html',
  standalone: false,
  styleUrls: ['./student-form.component.css']
})
export class StudentFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  regNo = 0;
  saving = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService,
    private toastService: ToastService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.form = this.fb.group({
      rollNo: ['', [Validators.required, Validators.min(1)]],
      name: ['', [Validators.required, Validators.minLength(2)]],
      standard: ['', [Validators.required]],
      school: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      percentage: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
    });

    const param = this.route.snapshot.params['regNo'];  
    this.isEdit = !!param;                            
    this.regNo = this.isEdit ? +param : 0;            

    if (this.isEdit) {
      this.loading = true;
      this.studentService.getOne(this.regNo).subscribe({
        next: (s) => {
          this.form.patchValue(s);
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
  }

  err(field: string): boolean {
    const c = this.form.get(field);
    return !!(c && c.invalid && (c.dirty || c.touched));
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;

    const req$ = this.isEdit
      ? this.studentService.update(this.regNo, this.form.value)
      : this.studentService.create(this.form.value);

    req$.subscribe({
      next: (s) => {
        this.saving = false;
        this.toastService.success(this.isEdit ? 'Student updated!' : 'Student added!');
        this.router.navigate(['/students', s.regNo]);
      },
      error: () => { this.saving = false; }
    });
  }

  cancel() {
    this.router.navigate(this.isEdit ? ['/students', this.regNo] : ['/students']);
  }
}