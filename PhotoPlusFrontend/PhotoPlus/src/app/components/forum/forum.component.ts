import { Component, OnInit } from '@angular/core';
import { Section } from 'src/app/models/section/section';
import { SectionService } from 'src/app/services/section/section.service';
import { LoginService } from 'src/app/services/login/login.service';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.scss']
})
export class ForumComponent implements OnInit {

  sections: Section[];

  constructor(private sectionService: SectionService, private loginService: LoginService) {}

  ngOnInit(): void {
    this.sectionService.getAll().subscribe(
      (data: Section[]) => {
        this.sections = data;
      });
  }

  get auth():LoginService {
    return this.loginService;
  }
}
