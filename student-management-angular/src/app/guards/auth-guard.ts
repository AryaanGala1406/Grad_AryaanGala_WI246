import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { User } from '../services/user';

export const authGuard: CanActivateFn = (route, state) => {
  let ur = inject(User)
  if(ur.getName() === "Guest")
    return false;
  else if(route.data[0] == ur.getRole() ||route.data[1] == ur.getRole())
    return true;
  else 
    return false;
};
