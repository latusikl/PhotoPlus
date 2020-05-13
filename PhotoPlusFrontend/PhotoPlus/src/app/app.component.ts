import { Component, ViewChild} from '@angular/core';
import { LoginService } from './services/login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private loginService: LoginService) {
  }

  get auth(): LoginService {
    return this.loginService;
  }

  title = 'PhotoPlus';


  logout() {
    this.loginService.logout();
    window.location.reload();
  }
}
