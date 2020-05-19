import { Component, OnInit, Input } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Topic } from "src/app/models/topic/topic";
import { UserService } from "src/app/services/user/user.service";
import { User } from "src/app/models/user/user";
import { LoginService } from "src/app/services/login/login.service";
import { Subject, BehaviorSubject } from "rxjs";

@Component({
  selector: "app-topic-header",
  templateUrl: "./topic-header.component.html",
  styleUrls: ["./topic-header.component.scss"],
})
export class TopicHeaderComponent implements OnInit {
  @Input("topic")
  topic: Topic;

  @Input("disableControls")
  disableControls: boolean;

  canModify: Subject<boolean>;
  canDelete: Subject<boolean>;

  constructor(private loginService: LoginService) {}

  ngOnInit(): void {
    this.canModify = new BehaviorSubject(this.loginService.isModerator);
    this.canDelete = new BehaviorSubject(
      !this.disableControls && 
      (this.loginService.getLoggedUser().code === this.topic.userCode ||
      this.loginService.isModerator));
  }

  breakableName(name:string){
    var chuncks = name.match(/.{1,10}/g);
    return chuncks.join("\xAD");
  }
}
