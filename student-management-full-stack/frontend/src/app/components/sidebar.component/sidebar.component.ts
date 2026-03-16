import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: false,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  constructor(public router: Router) {}

  links = [
    { label: 'All Students', route: '/students' },
    { label: 'Add Student',  route: '/students/new' },
    { label: 'Results',      route: '/students/results' },
    { label: 'Analytics',    route: '/students/analytics' },
  ];

  isActive(route: string): boolean {
    if (route === '/students') {
      return this.router.url === '/students';
    }
    return this.router.url.startsWith(route);
  }
}