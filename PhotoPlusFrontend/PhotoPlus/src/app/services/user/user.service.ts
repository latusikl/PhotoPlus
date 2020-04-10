import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../models/user/user';
import { AbstractService } from '../abstract-service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends AbstractService<User> {

  constructor(http: HttpClient) {
    super(http, 'http://localhost:8090/user')
  }

}
