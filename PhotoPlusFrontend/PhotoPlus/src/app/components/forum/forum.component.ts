import { Component, OnInit } from '@angular/core';
import { Section } from 'src/app/models/section/section';
import { SectionService } from 'src/app/services/section/section.service';
import { LoginService } from 'src/app/services/login/login.service';
import { Role } from 'src/app/models/role/role.enum';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.scss']
})
export class ForumComponent implements OnInit {

  sections: Section[];

  constructor(private sectionService: SectionService) {}

  ngOnInit(): void {
    this.sectionService.getAll().subscribe(
      (data: Section[]) => {
        this.sections = data;
      });
  }
}
