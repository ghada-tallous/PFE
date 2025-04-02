import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { AuthentificationService } from "../services/authentification.service";

@Injectable({
    providedIn: 'root'
  })
  export class AuthGuard implements CanActivate {
  
    constructor(private authService: AuthentificationService, private router: Router) {}
  
    // canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    canActivate(): boolean {
      if (this.authService.isAuthenticated()) {
        return true;
      } else {
        this.router.navigate(['/login']);
        return false;
      }
    }
  }
  