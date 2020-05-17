import { Injectable } from '@angular/core';
import { Address } from '../../models/address/address';
import { environment } from '../../../environments/environment';
import { SuccessModalComponent } from '../../components/success-modal/success-modal.component';
import { HttpClient } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from '../login/login.service';
import { AbstractService } from '../abstract-service';
import { FieldChange } from '../patch/field-change';
import { PatchFieldChangeService } from '../patch/patch-field-change.service';

@Injectable({
    providedIn: 'root'
})
export class AddressService extends AbstractService<Address> {

    constructor(http: HttpClient, private modalService: NgbModal, private loginService: LoginService, private patchFieldChangeService: PatchFieldChangeService) {
        super(http, 'address');
    }

    ADDRESS_USER_EDIT_ENDPOINT = 'address/editAddress/';

    postAddressForLoggedUser(newAddressToPost: Address) {
        newAddressToPost.userCode = this.loginService.getLoggedUserCode();
        this._http.post<string>(environment.hostAddress + this.ADDRESS_USER_EDIT_ENDPOINT + this.loginService.getLoggedUserCode(), newAddressToPost).subscribe(res => {
            const modalRef = this.modalService.open(SuccessModalComponent);
            modalRef.componentInstance.title = 'New address added!';
            modalRef.result.then(() => {
                location.reload();
            }, () => {
                location.reload();
            });
        });
    }

    deleteAddressForLoggedUser(addressCode: string) {
        this._http.delete(environment.hostAddress + this.ADDRESS_USER_EDIT_ENDPOINT + addressCode).subscribe(res => {
            const modalRef = this.modalService.open(SuccessModalComponent);
            modalRef.componentInstance.title = 'Address deleted!';
            modalRef.result.then(() => {
                location.reload();
            }, () => {
                location.reload();
            });
        });
    }

    patchAddressForLoggedUser(fieldChange: FieldChange, addressCode: string) {
        this.patchFieldChangeService.sendPatchRequest(this.ADDRESS_USER_EDIT_ENDPOINT + addressCode, fieldChange);
    }
}
