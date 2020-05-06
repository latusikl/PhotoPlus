import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {LoggedUser} from "../../models/login/logged-user.model";
import {UserModel} from "../../models/user/user-model";
import {Address} from "../../models/address/address";

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit {

    constructor(private http: HttpClient) {
    }

    loggedUserWithDetails: UserModel;
    loggedUser: LoggedUser;
    addresses: Address[];
    hostAddress = environment.hostAddress;

    ngOnInit(): void {
        this.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
        this.getLoggedUser().subscribe(userModel => {
            this.loggedUserWithDetails = userModel;
        });
        this.getUserAddresses();
    }

    function(): void {
        this.getUserAddresses();
        console.log(this.addresses);
    }

    getUserAddresses(): void {
        this.http.get<Address[]>(environment.hostAddress + "address/byUser/" + this.loggedUser.code).subscribe((res: Address[]) => this.addresses = res);
    }

    public getLoggedUser(): Observable<UserModel> {
        return this.http.get<UserModel>(this.hostAddress + "user/" + this.loggedUser.code);
    }

    get a() {
        return this.addresses;
    }

}
