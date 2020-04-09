import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../models/user/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userEndpoint = 'http://localhost:8090/user'

  constructor(private http:HttpClient) { }

  registerUser(user: User): Observable<Object> {
    return this.http.post('http://localhost:8090/user', [user]) // ! Trzeba podawać tablicę xD
  }

}
