import { Component, OnInit, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { User } from 'src/app/models/user/user';
import { UserService } from 'src/app/services/user/user.service';
import { Role } from 'src/app/models/role/role.enum';

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

  sendUpdateRole(userCode: string){
    console.log("updateRole", userCode, this.users.find((x)=> x.value.code === userCode).value.role);
    const idx = this.users.findIndex((x)=> x.value.code === userCode);
    const patchMsg = {role: this.users[idx].value.role} as User|any;
    this.userService.patch(parseInt(userCode), patchMsg).subscribe(()=> {
      alert("Change successful");
    })
  }

  changeRoleInModel(userCode: string, role: Role){
    const idx = this.users.findIndex((x)=> x.value.code === userCode);
    this.users[idx].value.role = role;    
  }
  
  get roleClass(): Role[]{
    return Object.values(Role) as Role[];
  }
}