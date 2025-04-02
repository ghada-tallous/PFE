import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { User } from '../model/user.model';

export interface SignInRequest {
  email: string;
  password: string;
}

export interface SignUpRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface JwtAuthentificationResponse {
  token: string;
  refreshToken: string;
}

export interface PasswordResetRequest {
  email: string;
}

export interface PasswordResetConfirmRequest {
  newPassword: string;
}

@Injectable({
  providedIn: 'root'
})

export class AuthentificationService {

  private baseUrl = 'http://localhost:8080/api/v1/auth';
  private tokenKey = 'auth_token';
  private emailKey = 'user_email';

  constructor(private http : HttpClient, private router: Router) { }

  signIn(request: SignInRequest): Observable<JwtAuthentificationResponse> {
    return this.http.post<JwtAuthentificationResponse>(`${this.baseUrl}/signin`, request).pipe(
      tap(response => {
        this.saveToken(response.token);
        this.saveEmail(request.email);
      })
    );
  }
  
  signUp(request: SignUpRequest): Observable<any> {
    return this.http.post<JwtAuthentificationResponse>(`${this.baseUrl}/signup`, request).pipe(
      tap(response => {
        this.saveToken(response.token);
        this.saveEmail(request.email);
      })
    );
  }

  // Demander un lien de réinitialisation
  forgotPassword(request: PasswordResetRequest): Observable<string> {
    return this.http.post(`${this.baseUrl}/forgot-password`, request, { responseType: 'text' });
  }

  // Confirmer la réinitialisation du mot de passe
  resetPassword(token: string, request: PasswordResetConfirmRequest): Observable<string> {
    return this.http.post(`${this.baseUrl}/reset-password/confirm?token=${token}`, request, { responseType: 'text' });
  }

  getUser(email: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${email}`);}
    
  saveToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken() {
    return localStorage.getItem(this.tokenKey);
  }

  saveEmail(email: string) {
    localStorage.setItem(this.emailKey, email);
  }

  getEmail() {
    return localStorage.getItem(this.emailKey);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.emailKey);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token != null;
  }
  confirmEmail(token: string): Observable<string> {
    return this.http.get(`${this.baseUrl}/confirm?token=${token}`, { responseType: 'text' });
  }
  /* refreshToken(): Observable<JwtAuthentificationResponse> {
    return this.http.post<JwtAuthentificationResponse>(`${this.baseUrl}/refresh`, {});
  } */

  /* signOut(): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/signout`, {});
  } */

  /* isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  } */

  /* getJwtToken(): string {
    return localStorage.getItem('token');
  } */

  /* getRefreshToken(): string {
    return localStorage.getItem('refreshToken');
  } */

  /* removeTokens(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  } */

  /* storeJwtToken(token: string): void {
    localStorage.setItem
  } */

  /* storeTokens(response: JwtAuthentificationResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('refreshToken', response.refreshToken);
  } */

    
}
