import { Component, OnInit, ViewChild, ElementRef, Renderer2, AfterContentInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Topic } from 'src/app/models/topic/topic';
import { TopicService } from 'src/app/services/topic/topic.service';
import { Post } from 'src/app/models/post/post';
import { PostService } from 'src/app/services/post.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-topic-body',
  templateUrl: './topic-body.component.html',
  styleUrls: ['./topic-body.component.scss']
})
export class TopicBodyComponent implements OnInit,AfterViewInit {

  @ViewChild("topicName",{static: false})
  titleTextarea:ElementRef;

  topic: BehaviorSubject<Topic|any>;
  posts: BehaviorSubject<Post>[];
  topicName: string;

  modifyingTopic:boolean;

  constructor(private rd:Renderer2, private topicService:TopicService, private postService: PostService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.topic = new BehaviorSubject({})
    this.posts = new Array<BehaviorSubject<Post>>();
    this.activatedRoute.params.subscribe(params =>{
      let topicCode = params["topicCode"];
      this.topicService.getSingle(topicCode).subscribe(topicData => {
        this.topic.next(topicData);
      })
      this.postService.getAllFromTopic(topicCode).subscribe(postsData => {
        
        for(let post of postsData){          
          this.posts.push(new BehaviorSubject(post));
        }
      })
    })
  }

  ngAfterViewInit(){
  }

  editTopic(){
    this.modifyingTopic = !this.modifyingTopic;
  }
  // Could be same function but i cant find suitable name
  cancelEdit(){
    this.modifyingTopic = !this.modifyingTopic;
  }

  deleteTopic(){
    this.router.navigate(["/forum/remove/topic", this.topic.getValue().code]);
  }

  saveChanges(){
    const editTitleSnapshot = this.titleTextarea.nativeElement;
    const newTitle = editTitleSnapshot.value;
    
    this.topicService.patch(parseInt(this.topic.getValue().code), {...this.topic.value, name: newTitle}).subscribe(() =>{
      this.topicService.getSingle(parseInt(this.topic.value.code)).subscribe(newTopicData => {
        this.topic.next(newTopicData);
      })
    })
    this.modifyingTopic = !this.modifyingTopic;
  }


}
