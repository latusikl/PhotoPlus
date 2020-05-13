import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../models/user/user';
import { AbstractService } from '../abstract-service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService extends AbstractService<User> {

  constructor(http: HttpClient) {
    super(http, 'user')
  }

  getUsersSearchByLogin(searchText:string): Observable<User[]>{
    return this._http.get<User[]>(this.hostAddress + this.endpointUrl + "/search/" + searchText);
  }

}
