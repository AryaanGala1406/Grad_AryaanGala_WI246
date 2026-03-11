import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './login/login';
import { Failure } from './failure/failure';
import { AdminMenu } from './admin-menu/admin-menu';
import { Home } from './home/home';
import { ShowStudents } from './show-students/show-students';
import { StudentService } from './services/student';
import { CreateStudent } from './create-student/create-student';
import { User } from './services/user';
import { RemoveStudent } from './remove-student/remove-student';
import { UpdateStudent } from './update-student/update-student';
import { StaffMenu } from './staff-menu/staff-menu';

@NgModule({
  declarations: [
    App,
    Login,
    Failure,
    AdminMenu,
    Home,
    RemoveStudent,
    UpdateStudent,
    ShowStudents,
    CreateStudent,
    StaffMenu,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners(), StudentService, User],
  bootstrap: [App],
})
export class AppModule {}
