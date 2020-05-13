import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {LoggedUser} from "../../models/login/logged-user.model";
import {UserModel} from "../../models/user/user-model";
import {AddressDto} from "../../models/address/address-dto";

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
    addresses: AddressDto[];
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
        this.http.get<AddressDto[]>(environment.hostAddress + "address/byUser/" + this.loggedUser.code).subscribe((res: AddressDto[]) => this.addresses = res);
    }

    public getLoggedUser(): Observable<UserModel> {
        return this.http.get<UserModel>(this.hostAddress + "user/editAccount/" + this.loggedUser.code);
    }

    get a() {
        return this.addresses;
    }

}
