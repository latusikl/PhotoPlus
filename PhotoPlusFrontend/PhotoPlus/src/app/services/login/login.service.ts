import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { LoginModel } from "../../models/login/login-model.model";
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { LoggedUser } from 'src/app/models/login/logged-user.model';
import { Role } from 'src/app/models/role/role.enum';
@Injectable({
    providedIn: 'root'
})
export class LoginService {

    private loggedPersonLogin: BehaviorSubject<string>;
    private hostAddress = environment.hostAddress;
    private jwtHelper = new JwtHelperService();
    private loggedUser: LoggedUser | any;

    constructor(private http: HttpClient, private router: Router) {

      try{
        this.loggedUser = JSON.parse(sessionStorage.getItem("loggedUser"));
      } catch {
        this.loggedUser = null;
      }
        if (this.isLoggedIn()) {
          this.loggedPersonLogin = new BehaviorSubject<string>(sessionStorage.getItem("login"));
        } else {
          this.loggedPersonLogin = new BehaviorSubject<string>('');
        }
    }

    login(login: string, password: string) {
        const loginModel: LoginModel = {login: login, password: password};
        console.log("sending post");

        this.http.post<HttpResponse<LoginModel>>(this.hostAddress + 'login', {
            login: login,
            password: password
        }, {observe: 'response'}).subscribe(res => {
          this.loggedUser = res.body;
          this.readTokenFromResponse(res);
          console.log(res.body['login']);
          sessionStorage.setItem("loggedUser", JSON.stringify(this.loggedUser));
          this.loggedPersonLogin.next(res.body['login']);
          //saving login in session storage
            //remove duplicate login !!
          sessionStorage.setItem("login", this.loggedPersonLogin.value);
          this.router.navigate(['/']);
        });
    }

    public logout() {
        sessionStorage.removeItem("token")
        sessionStorage.removeItem("date")
        sessionStorage.removeItem("loggedUser");
        this.http.get(this.hostAddress + 'logout');
        this.loggedPersonLogin.next("");
    }

    readTokenFromResponse(res) {
        const token
            = res.headers.get("Authorization");
        const date = res.headers.get("Expires");
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("date", date);
    }

    public isLoggedIn() {
        const token = sessionStorage.getItem("token")
        if (token == null) {
            return false
        }

        return !this.jwtHelper.isTokenExpired(token);
    }

    getLoggedPersonLogin(): Observable<string> {
      return this.loggedPersonLogin.asObservable();
    }

    getLoggedUser(): LoggedUser {
      return this.loggedUser;
    }

    get isModerator(): boolean{
      const role = this.getLoggedUser().role;
      return role === Role.ADMIN || role === Role.EMPLOYEE;
    }
  
    get isAdmin(): boolean{
      const role = this.getLoggedUser().role;
      return role === Role.ADMIN;
    }
}
