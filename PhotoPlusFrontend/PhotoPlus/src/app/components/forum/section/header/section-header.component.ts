import { Component, OnInit, Input } from '@angular/core';
import { Section } from 'src/app/models/section/section';
import { LoginService } from 'src/app/services/login/login.service';
import { Role } from 'src/app/models/role/role.enum';
import { Router } from '@angular/router';

@Component({
  selector: 'app-section',
  templateUrl: './section-header.component.html',
  styleUrls: ['./section-header.component.scss']
})
export class SectionComponent implements OnInit {

  @Input("section")
  section: Section;
  
  @Input("shouldModeratorButtonsBeRenedered")
  shouldModeratorButtonsBeRenedered:boolean;

  constructor(private loginService:LoginService, private router: Router) { }

  ngOnInit(): void {
  }
  get auth(): LoginService{
    return this.loginService;
  }
}
