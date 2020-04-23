import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Section } from 'src/app/models/section/section';
import { SectionService } from 'src/app/services/section/section.service';
import { LoginService } from 'src/app/services/login/login.service';
import { Topic } from 'src/app/models/topic/topic';

@Component({
  selector: 'app-section-body',
  templateUrl: './section-body.component.html',
  styleUrls: ['./section-body.component.scss']
})
export class SectionBodyComponent implements OnInit {

  section: Topic[];

  constructor(private loginService: LoginService,private activatedRoute: ActivatedRoute, private sectionService: SectionService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      const sectionCode = params['code'];
      
    })
  }

  get auth(): LoginService{
    return this.loginService;
  }

}
