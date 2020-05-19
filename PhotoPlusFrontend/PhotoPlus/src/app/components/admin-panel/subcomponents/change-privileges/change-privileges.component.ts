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

  @ViewChild("searchInput", { static: true })
  el: ElementRef;

  users: BehaviorSubject<User>[];

  selectedPage: BehaviorSubject<number>;
  amountOfPages: BehaviorSubject<number>;

  howMuchMilisecBeforeFetch = 500;
  searchbarInputTimer: NodeJS.Timeout;

  constructor(private userService: UserService, private renderer: Renderer2) { }

  async ngOnInit() {
    this.selectedPage = new BehaviorSubject(0);
    this.searchbarInputTimer = null;
    this.amountOfPages = new BehaviorSubject(0);
    const pageInfo = this.userService.getPageCount().toPromise();
    this.loadUsers();
    this.setupSearchBarListener();
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  setupSearchBarListener() {
    this.renderer.listen(this.el.nativeElement, 'input', () => {
      clearTimeout(this.searchbarInputTimer);
      const searchText: string = this.el.nativeElement.value;
      if (searchText === '') {
        this.loadUsers();
        return;
      }
      if (searchText.length > 2) {
        this.searchbarInputTimer = setTimeout(() => {
          this.getFilteredUsers(searchText);
        }, this.howMuchMilisecBeforeFetch);
      }
    });
  }

  getFilteredUsers(searchText: string) {
    this.userService.getUsersSearchByLogin(searchText).subscribe(users => {
      this.users = new Array();
      for (const user of users) {
        this.users.push(new BehaviorSubject(user));
      }
    })
  }

  loadUsers() {
    this.users = new Array<BehaviorSubject<User>>();
    this.userService.getPage(this.selectedPage.value).subscribe((data) => {
      for (const user of data) {
        this.users.push(new BehaviorSubject(user));
      }
    });
  }

  changePage(page: number) {
    this.selectedPage.next(page);
    this.loadUsers();
  }

  sendUpdateRole(userCode: string) {
    const user = this.users.find((x) => x.value.code === userCode);
    const patchMsg = { role: user.value.role } as User | any;
    this.userService.patch(userCode, patchMsg).subscribe(() => {
      alert('Change successful');
    })
  }

  changeRoleInModel(userCode: string, role: Role) {
    const user = this.users.find((x) => x.value.code === userCode);
    user.value.role = role;
  }

  get roleClass(): Role[] {
    return Object.values(Role) as Role[];
  }
}
