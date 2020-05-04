import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SectionService } from 'src/app/services/section/section.service';
import { TopicService } from 'src/app/services/topic/topic.service';
import { PostService } from 'src/app/services/post/post.service';
import { Topic } from 'src/app/models/topic/topic';
import { LoginService } from 'src/app/services/login/login.service';
import { Post } from 'src/app/models/post/post';

@Component({
  selector: 'app-topic-add',
  templateUrl: './topic-add.component.html',
  styleUrls: ['./topic-add.component.scss']
})
export class TopicAddComponent implements OnInit {

  sectionCode: string;
  topicForm:FormGroup;
  submitted:boolean;

  constructor(private formBuilder: FormBuilder,private sectionService:SectionService, private router: Router,private loginService:LoginService,
              private activatedRoute:ActivatedRoute, private topicService:TopicService, private postService: PostService) { }
  
  ngOnInit(): void {
    this.topicForm = this.formBuilder.group({
      title: ['',Validators.required],
      post: ['', Validators.required],
    });
    this.activatedRoute.params.subscribe(params => {
      this.sectionCode = params["sectionCode"];
    })
  }

  get f(){
    return this.topicForm.controls;
  }

  onSubmit(){
    this.submitted = true;
    if(this.topicForm.invalid){
      return;
    }
    const form = this.topicForm.value;
    const topic: Topic = {
      code: null,
      name: form.title,
      sectionCode: this.sectionCode,
      date: new Date(),
      userCode: this.loginService.getLoggedUser().code
    }
    this.topicService.post(topic).subscribe(topicData => {
      const post:Post = {
        topicCode: topicData[0] as any,
        userCode: this.loginService.getLoggedUser().code,
        date: new Date(),
        content: form.post
      }
      this.postService.post(post).subscribe(postData => {
        this.router.navigate(["/forum/topic", topicData[0]]);
      });
    });
    
  }
}
