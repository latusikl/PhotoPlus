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

  selectedPage: BehaviorSubject<number>;
  amountOfPages: BehaviorSubject<number>;

  howMuchMilisecBeforeFetch: number = 500; 
  searchbarInputTimer: NodeJS.Timeout;

  constructor(private userService: UserService, private renderer: Renderer2) {}

  async ngOnInit() {
    this.selectedPage = new BehaviorSubject(0);
    this.searchbarInputTimer = null;
    this.amountOfPages = new BehaviorSubject(0);
    let pageInfo = this.userService.getPageCount().toPromise();
    this.loadUsers();
    this.setupSearchBarListener();
    let info = await pageInfo;
    this.amountOfPages.next((await pageInfo).pageAmount);
    console.log(info);
  }

  setupSearchBarListener(){
    this.renderer.listen(this.el.nativeElement,"input",() => {
      clearTimeout(this.searchbarInputTimer);
      const searchText = this.el.nativeElement.value;
      if(searchText === ''){
        this.loadUsers();
        return;
      }
      this.searchbarInputTimer = setTimeout(()=>{
        this.getFilteredUsers(searchText);
      },1000)
    });
  }

  getFilteredUsers(searchText: string){
    this.userService.getUsersSearchByLogin(searchText).subscribe(users =>{
      this.filteredUsers = new Array();
      for(const user of users){
        this.filteredUsers.push(new BehaviorSubject(user));
      }
    })
  }

  loadUsers(){
    this.users = new Array<BehaviorSubject<User>>();
    this.filteredUsers = new Array<BehaviorSubject<User>>();
    this.userService.getPage(this.selectedPage.value).subscribe((data) => {
      for (let user of data) {
        this.users.push(new BehaviorSubject(user));
      }
      this.filteredUsers = this.users;
    });
  }

  changePage(page:number){
    this.selectedPage.next(page);
    this.loadUsers();
  }

  sendUpdateRole(userCode: string){
    const idx = this.users.findIndex((x)=> x.value.code === userCode);
    const patchMsg = {role: this.users[idx].value.role} as User|any;
    this.userService.patch(userCode, patchMsg).subscribe(()=> {
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