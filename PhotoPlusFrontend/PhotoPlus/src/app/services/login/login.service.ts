import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { LoginModel } from '../../models/login/login-model.model';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { LoggedUser } from 'src/app/models/login/logged-user.model';
import { Role } from 'src/app/models/role/role.enum';

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    private hostAddress = environment.hostAddress;
    private jwtHelper = new JwtHelperService();
    private loggedUser: LoggedUser | any;

    constructor(private http: HttpClient, private router: Router) {

        try {
            this.loggedUser = JSON.parse(localStorage.getItem('loggedUser'));
            const token = localStorage.getItem('token');
            if (this.jwtHelper.isTokenExpired(token)) {
                localStorage.removeItem('token');
                localStorage.removeItem('loggedUser');
            }
        } catch {
            this.loggedUser = null;
        }
    }

    login(login: string, password: string) {
        const loginModel: LoginModel = { login: login, password: password };

        this.http.post<HttpResponse<LoginModel>>(this.hostAddress + 'login', {
            login: login,
            password: password
        }, { observe: 'response' }).subscribe(res => {
            this.loggedUser = res.body;
            this.readTokenFromResponse(res);
            localStorage.setItem('loggedUser', JSON.stringify(this.loggedUser));
            this.router.navigate(['/']);
        });
    }

    public logout() {
        localStorage.removeItem('token')
        localStorage.removeItem('loggedUser');
        this.http.get(this.hostAddress + 'logout');
    }

    readTokenFromResponse(res) {
        const token = res.headers.get('Authorization');
        localStorage.setItem('token', token);
    }

    public isLoggedIn() {
        const token = localStorage.getItem('token')
        if (token == null) {
            return false;
        } else if (this.jwtHelper.isTokenExpired(token)) {
            localStorage.removeItem('token');
            localStorage.removeItem('loggedUser');
            this.loggedUser = null;
            return false;
        }
        return true;
    }

    getLoggedUser(): LoggedUser {
        return this.loggedUser;
    }

    getLoggedUserCode(): string {
        return this.loggedUser.code;
    }

    get isModerator(): boolean {
        const role = this.getLoggedUser()?.role;
        return role === Role.ADMIN || role === Role.EMPLOYEE;
    }

    get isAdmin(): boolean {
        const role = this.getLoggedUser()?.role;
        return role === Role.ADMIN;
    }
}
