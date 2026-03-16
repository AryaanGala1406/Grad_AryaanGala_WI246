import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  search = '';
  constructor(private router: Router) {}

  onSearch() {
    if (this.search.trim()) {
      this.router.navigate(['/students'], {
        queryParams: { q: this.search }
      });
    }
  }
}