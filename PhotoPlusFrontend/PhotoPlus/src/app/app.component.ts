import { Component } from '@angular/core';
import { LoginService } from './services/login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'PhotoPlus';


  constructor(private loginService: LoginService) {
  }

  logout() {
    this.loginService.logout();
    window.location.reload();
  }

  get auth(): LoginService {
    return this.loginService;
  }
}
