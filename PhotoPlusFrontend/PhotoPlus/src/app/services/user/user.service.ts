import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../models/user/user';
import {AbstractService} from '../abstract-service';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {LoginService} from '../login/login.service';
import {Address} from '../../models/address/address';
import {FieldChange} from '../patch/field-change';
import {PatchFieldChangeService} from '../patch/patch-field-change.service';

@Injectable({
    providedIn: 'root'
})
export class UserService extends AbstractService<User> {

    constructor(http: HttpClient, private loginService: LoginService, private patchFieldChangeService: PatchFieldChangeService) {
        super(http, 'user');
    }

    getUsersSearchByLogin(searchText: string): Observable<User[]> {
        return this._http.get<User[]>(this.hostAddress + this.endpointUrl + '/search/' + searchText);
    }

    getDetailsOfLoggedUser(): Observable<User> {
        return this._http.get<User>(this.hostAddress + 'user/editAccount/' + this.loginService.getLoggedUserCode());
    }

    getAddressesOfLoggedUser(): Observable<Address[]> {
        return this._http.get<Address[]>(environment.hostAddress + 'address/byUser/' + this.loginService.getLoggedUserCode());
    }

    patchDetailsOfLoggedUser(fieldChange: FieldChange) {
        this.patchFieldChangeService.sendPatchRequest('user/editAccount/' + this.loginService.getLoggedUserCode(), fieldChange);
    }
}
