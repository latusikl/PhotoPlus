import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {LoginModel} from "../models/login/login-model.model";

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    constructor(private http: HttpClient) {
    }

    login(login: string, password: string) {
        const loginModel: LoginModel = {login: login, password: password};
        console.log("sending post");

        this.http.post<HttpResponse<LoginModel>>('http://localhost:8090/login', {
            login: login,
            password: password
        }, {observe: 'response'}).subscribe(res => this.readTokenFromResponse(res));

    }

    public logout() {
        localStorage.removeItem("token")
        localStorage.removeItem("date")
        this.http.get('http://localhost:8090/logout');
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
}
