import { Component, OnInit } from '@angular/core';

import { environment } from '../../../environments/environment';
import { LoggedUser } from '../../models/login/logged-user.model';
import { LoginService } from '../../services/login/login.service';
import { UserService } from '../../services/user/user.service';
import { User } from '../../models/user/user';
import { Address } from '../../models/address/address';

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit {

    constructor(private loginService: LoginService, private userService: UserService) {
    }

    loggedUserWithDetails: User;
    loggedUser: LoggedUser;
    addresses: Address[];
    hostAddress = environment.hostAddress;

    ngOnInit(): void {
        this.loggedUser = this.loginService.getLoggedUser();
        this.userService.getDetailsOfLoggedUser().subscribe(userModel => {
            this.loggedUserWithDetails = userModel;
        });
        this.userService.getAddressesOfLoggedUser().subscribe((res: Address[]) => this.addresses = res);
    }

    get a() {
        return this.addresses;
    }

}
