import { Component, OnInit } from '@angular/core';
import { TopicService } from 'src/app/services/topic/topic.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { Topic } from 'src/app/models/topic/topic';

@Component({
  selector: 'app-topic-edit',
  templateUrl: './topic-edit.component.html',
  styleUrls: ['./topic-edit.component.scss']
})
export class TopicEditComponent implements OnInit {

  topic:Topic;
  topicForm:FormGroup;
  submitted:boolean;

  constructor(private formBuilder:FormBuilder,private activatedRoute: ActivatedRoute, private topicService: TopicService, private router:Router) { }
  
  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let topicCode = params["topicCode"];
      this.topicService.getSingle(topicCode).subscribe(topicData => {  
        this.topic = topicData;      
        this.topicForm = this.formBuilder.group({
          title: [topicData.name,Validators.required],
        });
      })
    })
    
  }

  get f(){
    return this.topicForm.controls;
  }

  async onSubmit(){
    this.submitted = true;
    if(this.topicForm.invalid){
      return;
    }
    const form = this.topicForm.value;
    this.topicService.patch(parseInt(this.topic.code),{...this.topic, name: form.title }).subscribe(()=>{
      this.router.navigate(["/forum/section", this.topic.sectionCode]);
    });
  }
}
