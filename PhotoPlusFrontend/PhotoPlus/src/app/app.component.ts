import { Component } from '@angular/core';
import { LoginService } from './services/login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'PhotoPlus';
  loggedPersonLogin: string;


  constructor(private loginService: LoginService) {
    this.loginService.getLoggedPersonLogin().subscribe(value => this.loggedPersonLogin = value);
  }

  logout() {
    this.loginService.logout();
    window.location.reload();
  }

  get isLoggedIn(): boolean {
    return this.loginService.isLoggedIn();
  }
}
