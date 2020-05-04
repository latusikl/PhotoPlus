import { Component, OnInit, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { User } from 'src/app/models/user/user';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-change-privileges',
  templateUrl: './change-privileges.component.html',
  styleUrls: ['./change-privileges.component.scss']
})
export class ChangePrivilegesComponent implements OnInit {

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
        if(user.login === "admin"){
          continue;
        }
        this.users.push(new BehaviorSubject(user));
      }
      this.filteredUsers = this.users;
    });
    this.renderer.listen(this.el.nativeElement,"input",() => {
      const searchText = this.el.nativeElement.value;
      if(searchText == ''){
        this.filteredUsers = this.users;
        return;
      }
      this.filteredUsers = this.users.filter((x) => 
        x.value.login.includes(searchText) || x.value.code.includes(searchText)
      );
    });
  }

  updateUserRole(user: BehaviorSubject<User>){
    console.log("updateRole");
    
  }
}