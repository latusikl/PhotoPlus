import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Topic } from 'src/app/models/topic/topic';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/models/user/user';
import { LoginService } from 'src/app/services/login/login.service';

@Component({
  selector: 'app-topic-header',
  templateUrl: './topic-header.component.html',
  styleUrls: ['./topic-header.component.scss']
})
export class TopicHeaderComponent implements OnInit {

  @Input("topic")
  topic: Topic;
  canModify: boolean;
  topicOwnerName: User;
  
  constructor(private userService:UserService, private loginService: LoginService, private activatedRoute:ActivatedRoute, private router:Router) { }

  ngOnInit(): void {
    this.userService.getSingle(parseInt(this.topic.userCode)).subscribe(data=> {
      this.topicOwnerName = data;
    });
    this.canModify = this.loginService.getLoggedUser().login === this.topicOwnerName.login || this.loginService.isModerator;
  }
}
