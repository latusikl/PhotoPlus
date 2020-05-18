import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Post } from 'src/app/models/post/post';
import { PostService } from 'src/app/services/post/post.service';
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
  post: BehaviorSubject<Post>;

  @Output("reloadParentFunction")
  onDeleted:EventEmitter<any> = new EventEmitter();

  postOwner: BehaviorSubject<User|any>;

  duringModification:boolean;

  constructor(private postService:PostService, private loginService:LoginService, private userService:UserService) { }

  ngOnInit(): void {
    this.duringModification = false;
  }

  savePost(){
    const newPostContent = this.postEditTextarea.nativeElement.value;
    this.postService[this.auth.isModerator ? "patch":"patchOwn"](this.post.value.code, {...this.post.value, content: newPostContent}).subscribe(() => {
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
      this.postService[this.auth.isModerator? 'delete': "deleteOwn"](this.post.value.code).subscribe(()=>{
        this.onDeleted.next();
      })
    } else {
      return;
    }
  }

  breakableName(name:string){
    var chuncks = name.match(/.{1,10}/g);
    return chuncks.join("\xAD");
  }

  get canModify():boolean {
    return (this.post.value.userCode === this.loginService.getLoggedUser().code || this.loginService.isModerator);
  }

  get auth(): LoginService{
    return this.loginService;
  }

}
