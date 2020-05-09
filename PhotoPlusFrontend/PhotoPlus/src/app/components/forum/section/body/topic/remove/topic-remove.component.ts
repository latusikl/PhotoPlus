import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Topic } from 'src/app/models/topic/topic';
import { TopicService } from 'src/app/services/topic/topic.service';

@Component({
  selector: 'app-remove',
  templateUrl: './topic-remove.component.html',
  styleUrls: ['./topic-remove.component.scss']
})
export class TopicRemoveComponent implements OnInit {

  topic: Topic;

  constructor(private activatedRoute:ActivatedRoute, private topicService:TopicService, private router:Router, private location:Location) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let topicCode = params["topicCode"];
      this.topicService.getSingle(topicCode).subscribe(topicData => {
        this.topic = topicData;
      })
    })
  }

  deleteTopic(){
    this.topicService.delete(this.topic.code).subscribe((data:Topic)=>{
      this.router.navigate(['/forum/section', this.topic.sectionCode]);
    })
  }

  takeMeBack(){
    this.location.back();
  }
}
