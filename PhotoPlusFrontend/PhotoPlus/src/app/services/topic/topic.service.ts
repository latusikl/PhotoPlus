import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Topic } from '../../models/topic/topic';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TopicService extends AbstractService<Topic> {

  constructor(http:HttpClient) { 
    super(http, "topic");
  }
}
