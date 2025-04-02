import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/api/v1/user';
  constructor(private http : HttpClient) { }

  /* getUser(email: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${email}`);} */

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/update`, user);
  }

  uploadFile(formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/upload`, formData);
  }
}
