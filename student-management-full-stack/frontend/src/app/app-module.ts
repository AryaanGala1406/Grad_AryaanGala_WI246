import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { SidebarComponent } from './components/sidebar.component/sidebar.component';
import { NavbarComponent } from './components/navbar.component/navbar.component';
import { ToastComponent } from './components/toast.component/toast.component';
import { StudentListComponent } from './components/student-list.component/student-list.component';
import { StudentFormComponent } from './components/student-form.component/student-form.component';
import { StudentDetailComponent } from './components/student-detail.component/student-detail.component';
import { StudentResultsComponent } from './components/student-result.component/student-result.component';
import { StudentAnalyticsComponent } from './components/student-analytics.component/student-analytics.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StudentService } from './service/student-service';
import { ToastService } from './service/toast-service';

@NgModule({
  declarations: [
    App,
    SidebarComponent,
    NavbarComponent,
    ToastComponent,
    StudentListComponent,
    StudentFormComponent,
    StudentDetailComponent,
    StudentResultsComponent,
    StudentAnalyticsComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule, ReactiveFormsModule],
  providers: [provideBrowserGlobalErrorListeners(), StudentService, ToastService],
  bootstrap: [App],
})
export class AppModule {}
