import { Component, OnInit, Input } from '@angular/core';
import { Post } from 'src/app/models/post/post';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  @Input("post")
  post: Post;

  constructor() { }

  ngOnInit(): void {
  }

}
