import { Component, OnInit, Input } from '@angular/core';
import { Section } from 'src/app/models/section/section';
import { LoginService } from 'src/app/services/login/login.service';
import { Role } from 'src/app/models/role/role.enum';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.scss']
})
export class SectionComponent implements OnInit {

  @Input("section")
  section: Section;

  constructor(private loginService:LoginService) { }

  ngOnInit(): void {
  }

  get isModerator(): boolean{
    const role = this.loginService.getLoggedUser().role;
    return role === Role.ADMIN || role === Role.EMPLOYEE;
  }

  get isAdmin(): boolean{
    const role = this.loginService.getLoggedUser().role;
    return role === Role.ADMIN;
  }

}
