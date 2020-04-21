import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {LoginModel} from "../../models/login/login-model.model";
import {Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { ErrorModalComponent } from 'src/app/components/error-modal/error-modal.component';

@Injectable({
    providedIn: 'root'
})
export class LoginService {

    constructor(private http: HttpClient, private router: Router,
      private modalService: NgbModal) {
    }

    login(login: string, password: string) {
        const loginModel: LoginModel = {login: login, password: password};
        console.log("sending post");

        this.http.post<HttpResponse<LoginModel>>('http://localhost:8090/login', {
            login: login,
            password: password
        }, {observe: 'response'}).subscribe(res => {
          this.readTokenFromResponse(res);
          this.router.navigate(['/']);
        }, error => {
            const modalRef = this.modalService.open(ErrorModalComponent);
            modalRef.componentInstance.message = "Bad login or password. Please try again!";
            modalRef.componentInstance.title = "Error occured!";
      });
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
