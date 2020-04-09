import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../models/user/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userEndpoint = 'https://photo-plus.herokuapp.com/user'

  constructor(private http:HttpClient) { }

  registerUser(user: User): Observable<Object> {
    return this.http.post('https://photo-plus.herokuapp.com/user', [user]) // ! Trzeba podawać tablicę xD
  }

}
