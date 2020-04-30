import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Post } from 'src/app/models/post/post';
import { PostService } from 'src/app/services/post.service';
import { LoginService } from 'src/app/services/login/login.service';
import { UserService } from 'src/app/services/user/user.service';
import { BehaviorSubject } from 'rxjs';
import { User } from 'src/app/models/user/user';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  @ViewChild("postEdit",{static: false})
  postEditTextarea: ElementRef;

  @Input("post")
  post: BehaviorSubject<Post|any>;

  @Output("reloadParentFunction")
  onDeleted:EventEmitter<any> = new EventEmitter();

  postOwner: BehaviorSubject<User|any>;

  duringModification:boolean;

  constructor(private postService:PostService, private loginService:LoginService, private userService:UserService) { }

  ngOnInit(): void {
    this.duringModification = false;
    this.postOwner = new BehaviorSubject({});
    this.userService.getSingle(parseInt(this.post.value.userCode)).subscribe(userData => {
      this.postOwner.next(userData);
    })
  }

  savePost(){
    const newPostContent = this.postEditTextarea.nativeElement.value;
    this.postService.patch(this.post.value.code, {...this.post.value, content: newPostContent}).subscribe(() => {
      this.postService.getSingle(this.post.value.code).subscribe(data => {
        this.post.next(data);
      })
    })
    this.duringModification = !this.duringModification;
  }

  editPost(){
    this.duringModification = !this.duringModification;
  }

  removePost(){
    if(confirm("Are you sure that you want to remove post with content\n" + this.post.value.content)){
      this.postService.delete(this.post.value.code).subscribe(()=>{
        this.onDeleted.next();
      })
    } else {
      return;
    }
    
  }

  get canModify():boolean {
    const owner: User = this.postOwner.value || null;
    return (owner.code === this.loginService.getLoggedUser().code || this.loginService.isModerator);
  }

}
