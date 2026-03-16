import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StudentListComponent } from './components/student-list.component/student-list.component';
import { StudentFormComponent } from './components/student-form.component/student-form.component';
import { StudentResultsComponent } from './components/student-result.component/student-result.component';
import { StudentAnalyticsComponent } from './components/student-analytics.component/student-analytics.component';
import { StudentDetailComponent } from './components/student-detail.component/student-detail.component';

const routes: Routes = [
  { path: '', redirectTo: 'students', pathMatch: 'full' },
  { path: 'students', component: StudentListComponent },
  { path: 'students/new', component: StudentFormComponent },
  { path: 'students/results', component: StudentResultsComponent },
  { path: 'students/analytics', component: StudentAnalyticsComponent },
  { path: 'students/:regNo/edit', component: StudentFormComponent },
  { path: 'students/:regNo', component: StudentDetailComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
