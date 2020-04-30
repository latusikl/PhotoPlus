import { Component, OnInit, Input } from '@angular/core';
import { Topic } from 'src/app/models/topic/topic';
import { UserService } from 'src/app/services/user/user.service';
import { BehaviorSubject } from 'rxjs';
import { User } from 'src/app/models/user/user';
import { ActivatedRoute, Router } from '@angular/router';
import { TopicService } from 'src/app/services/topic/topic.service';

@Component({
  selector: 'app-remove',
  templateUrl: './topic-remove.component.html',
  styleUrls: ['./topic-remove.component.scss']
})
export class TopicRemoveComponent implements OnInit {

  topic: Topic;

  constructor(private activatedRoute:ActivatedRoute, private topicService:TopicService, private router:Router) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let topicCode = params["topicCode"];
      this.topicService.getSingle(parseInt(topicCode)).subscribe(topicData => {
        this.topic = topicData;
      })
    })
  }

  deleteTopic(){
    this.topicService.delete(this.topic.code).subscribe((data:Topic)=>{
      console.log(data);
      this.router.navigate(['/forum/section', this.topic.sectionCode]);
    })
  }

  takeMeBack(){
    this.router.navigate(['/forum/section', this.topic.sectionCode]);
  }
}
