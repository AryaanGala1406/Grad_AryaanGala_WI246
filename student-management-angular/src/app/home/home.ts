import { Component } from '@angular/core';
import { User } from '../services/user';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  constructor(public user: User) {
  }
}
