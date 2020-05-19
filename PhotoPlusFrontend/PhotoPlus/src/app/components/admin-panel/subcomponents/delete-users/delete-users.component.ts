import { Component, OnInit, ViewChild, ElementRef, Renderer2 } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/models/user/user';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-delete-users',
  templateUrl: './delete-users.component.html',
  styleUrls: ['./delete-users.component.scss'],
})
export class DeleteUsersComponent implements OnInit {

  @ViewChild('searchInput', { static: true })
  el: ElementRef;

  users: BehaviorSubject<User>[];

  amountOfPages: BehaviorSubject<number>;
  seletedPage: BehaviorSubject<number>;

  howMuchMilisecBeforeFetch = 500;
  searchbarInputTimer: NodeJS.Timeout;

  constructor(private userService: UserService, private renderer: Renderer2) { }

  async ngOnInit() {
    this.amountOfPages = new BehaviorSubject(0);
    this.seletedPage = new BehaviorSubject(0);
    this.searchbarInputTimer = null;
    const pageCount = this.userService.getPageCount().toPromise();
    this.amountOfPages.next((await pageCount).pageAmount);
    this.loadUsers();
    this.setupSearchBarListener()

  }

  changePage(event: number) {
    this.seletedPage.next(event);
    this.loadUsers();
  }

  loadUsers() {
    this.users = new Array<BehaviorSubject<User>>();
    this.userService.getPage(this.seletedPage.value).subscribe((data) => {
      for (const user of data) {
        this.users.push(new BehaviorSubject(user));
      }
    });
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

  deleteUser(userCode: string) {
    const user = this.users.find((x)=> x.value.code === userCode);
    
    if (confirm('Do you want to delete user: \n\n' + user.value.name + ' ' + user.value.surname)) {
      this.userService.delete(user.value.code).subscribe(() => {
        this.ngOnInit(); // reinicjalizcja
      });
    }
  }
}
