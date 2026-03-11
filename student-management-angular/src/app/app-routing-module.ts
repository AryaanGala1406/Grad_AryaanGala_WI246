import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './login/login';
import { Failure } from './failure/failure';
import { authGuard } from './guards/auth-guard';
import { Home } from './home/home';
import { CreateStudent } from './create-student/create-student';
import { ShowStudents } from './show-students/show-students';
import { AdminMenu } from './admin-menu/admin-menu';
import { RemoveStudent } from './remove-student/remove-student';
import { UpdateStudent } from './update-student/update-student';
import { StaffMenu } from './staff-menu/staff-menu';

const routes: Routes = [
  {
    // as we want this as the initial page 
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'failure',
    component: Failure
  },
  {
    path: 'home',
    component: Home
  },
  {
    path: 'create-student',
    component: CreateStudent,
    canActivate: [authGuard],
    data: ["Admin"]
  },
  {
    path: 'show-students',
    component: ShowStudents,
    canActivate: [authGuard],
    data: ["Admin", "Staff"]
  },
  {
    path: 'update-student',
    component: UpdateStudent,
    canActivate: [authGuard],
    data: ["Admin"]
  },
  {
    path: 'remove-student',
    component: RemoveStudent,
    canActivate: [authGuard],
    data: ["Admin"]
  },
  {
    path: 'admin-menu',
    component: AdminMenu,
    canActivate: [authGuard],
    data: ["Admin"]
  },
  {
    path: 'staff-menu',
    component: StaffMenu,
    canActivate: [authGuard],
    data: ["Staff"]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
