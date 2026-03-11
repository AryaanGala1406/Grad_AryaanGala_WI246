import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../services/user';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  constructor(private router: Router, public user: User) {}

  validate(event: any) {
    event.preventDefault() 
    let username = event.target.elements[0].value
    console.log("Username: " + username)
    let password = event.target.elements[1].value
    console.log("Password: " + password)
    let role = event.target.elements[2].value
    console.log("Role: " + role)

    if(username === password) {
      this.user.setName(username)
      this.user.setRole(role)
      if(this.user.getRole() == "Admin")
        this.router.navigate(['admin-menu']) 
      else if(this.user.getRole() == "Staff")
        this.router.navigate(['staff-menu']) 
    } else {
      this.router.navigate(['failure'])
    }
  }
}