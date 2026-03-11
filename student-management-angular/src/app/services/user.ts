import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class User {
  private name: string = "Guest"
  private role: string = "User"
  constructor() {}

  public setName(uname:string): void {
    this.name = uname
  }

  public getName(): string {
    return this.name
  }

  public setRole(role:string): void {
    this.role = role
  }

  public getRole(): string {
    return this.role
  }
}