import { Injectable } from '@angular/core';
import { AbstractService } from './abstract-service';
import { HttpClient } from '@angular/common/http';
import { Post } from '../models/post/post';

@Injectable({
  providedIn: 'root'
})
export class PostService extends AbstractService<Post> {

  constructor(private http: HttpClient) { 
    super(http, "post");
  }
}
