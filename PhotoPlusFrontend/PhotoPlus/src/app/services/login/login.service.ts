import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { LoginModel } from "../../models/login/login-model.model";
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    private loggedPersonLogin: BehaviorSubject<string>;

    private hostAddress = environment.hostAddress;

    constructor(private http: HttpClient, private router: Router) {

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

          this.readTokenFromResponse(res);
          console.log(res.body['login']);
          this.loggedPersonLogin.next(res.body['login']);
          //saving login in session storage
          sessionStorage.setItem("login", this.loggedPersonLogin.value);
          this.router.navigate(['/']);
        });
    }

    public logout() {
        localStorage.removeItem("token")
        localStorage.removeItem("date")
        this.http.get(this.hostAddress + 'logout');
        this.loggedPersonLogin.next("");
    }

    readTokenFromResponse(res) {
        const token = res.headers.get("Authorization");
        const date = res.headers.get("Expires");
        localStorage.setItem("token", token);
        localStorage.setItem("date", date);
    }

    public isLoggedIn() {
        if (localStorage.getItem("token") == null) {
            return false
        }
        //TODO:Check if not expired
        return true;
    }

    getLoggedPersonLogin(): Observable<string> {
      return this.loggedPersonLogin.asObservable();
    }
}
