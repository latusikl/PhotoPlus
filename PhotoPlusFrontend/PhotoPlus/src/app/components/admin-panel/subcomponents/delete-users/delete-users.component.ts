import { Component, OnInit, ViewChild, ElementRef, Renderer2 } from "@angular/core";
import { UserService } from "src/app/services/user/user.service";
import { User } from "src/app/models/user/user";
import { BehaviorSubject } from "rxjs";

@Component({
  selector: "app-delete-users",
  templateUrl: "./delete-users.component.html",
  styleUrls: ["./delete-users.component.scss"],
})
export class DeleteUsersComponent implements OnInit {

  @ViewChild("searchInput", {static: true})
  el: ElementRef;

  users: BehaviorSubject<User>[];
  filteredUsers: BehaviorSubject<User>[];

  constructor(private userService: UserService, private renderer: Renderer2) {}

  ngOnInit(): void {
    this.users = new Array<BehaviorSubject<User>>();
    this.filteredUsers = new Array<BehaviorSubject<User>>();
    this.userService.getAll().subscribe((data) => {
      for (let user of data) {
        this.users.push(new BehaviorSubject(user));
      }
      this.filteredUsers = this.users;
    });
    this.renderer.listen(this.el.nativeElement,"input",() => {
      const searchText = this.el.nativeElement.value;
      console.log(searchText);
      if(searchText == ''){
        this.filteredUsers = this.users;
        return;
      }
      this.filteredUsers = this.users.filter((x) => 
        x.value.login.includes(searchText) || x.value.code.includes(searchText)
      );
    });
  }
  deleteUser(user: BehaviorSubject<User>){
    // TODO
    console.log("delete", user.value.login);
  }
}
