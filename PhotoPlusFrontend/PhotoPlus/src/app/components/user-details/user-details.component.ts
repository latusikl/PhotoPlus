import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {LoggedUser} from "../../models/login/logged-user.model";
import {UserModel} from "../../models/user/user-model";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit {

  constructor(private http : HttpClient) { }

  public loggedUserWithDetails : UserModel;
  private loggedUser : LoggedUser;
  private hostAddress = environment.hostAddress;

  ngOnInit(): void {
    this.loggedUser = JSON.parse(sessionStorage.getItem("loggedUser"));
    this.getLoggedUser().subscribe(userModel => {this.loggedUserWithDetails = userModel;});
  }

  function() : void {
    console.log(this.loggedUserWithDetails);
  }

  public getLoggedUser() : Observable<UserModel>
  {
    return this.http.get<UserModel>(this.hostAddress + "user/" + this.loggedUser.code);
  }

}
