import { Component, OnInit, Inject, Input } from '@angular/core';
import { TopicService } from 'src/app/services/topic/topic.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Topic } from 'src/app/models/topic/topic';
import { UserService } from 'src/app/services/user/user.service';
import { Observable, BehaviorSubject, Subject, VirtualTimeScheduler } from 'rxjs';
import { User } from 'src/app/models/user/user';

@Component({
  selector: 'app-topic-header',
  templateUrl: './topic-header.component.html',
  styleUrls: ['./topic-header.component.scss']
})
export class TopicHeaderComponent implements OnInit {

  @Input("topic")
  topic: Topic;
  @Input("canModify")
  canModify: boolean;
  topicOwnerName: User;
  
  constructor(private userService:UserService, private topicService: TopicService, private activatedRoute:ActivatedRoute, private router:Router) { }

  ngOnInit(): void {
    this.userService.getSingle(parseInt(this.topic.userCode)).subscribe(data=> {
      this.topicOwnerName = data;
    });
  }
}
