import { Component, ViewChild } from '@angular/core';
import { LoginService } from './services/login/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private loginService: LoginService, private router: Router) {
  }

  get auth(): LoginService {
    return this.loginService;
  }

  title = 'PhotoPlus';
  searchedText = '';

  logout() {
    this.loginService.logout();
    window.location.reload();
  }

  canSearch() {
    return this.searchedText.length > 2;
  }

  navigateToSearch() {
    if (this.canSearch()) {
      this.router.navigate(['/search', this.searchedText]);
    }
  }
}
