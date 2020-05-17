import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Section } from 'src/app/models/section/section';
import { SectionService } from 'src/app/services/section/section.service';
import { LoginService } from 'src/app/services/login/login.service';
import { Topic } from 'src/app/models/topic/topic';
import { TopicService } from 'src/app/services/topic/topic.service';

@Component({
  selector: 'app-section-body',
  templateUrl: './section-body.component.html',
  styleUrls: ['./section-body.component.scss']
})
export class SectionBodyComponent implements OnInit {

  sectionCode: string;
  topics: Topic[];

  constructor(private loginService: LoginService, private activatedRoute: ActivatedRoute, private topicService: TopicService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.sectionCode = params.sectionCode;
      this.topicService.getAllFromSectionCode(this.sectionCode).subscribe(data => {
        this.topics = data;
      });
    });
  }

  get auth(): LoginService {
    return this.loginService;
  }

}
