import {Injectable} from '@angular/core';
import {AddressModel} from "../../models/address/address-model";
import {environment} from "../../../environments/environment";
import {SuccessModalComponent} from "../../components/success-modal/success-modal.component";
import {HttpClient} from "@angular/common/http";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Injectable({
    providedIn: 'root'
})
export class UserAddressService {

    constructor(private http: HttpClient, private modalService: NgbModal) {
    }

    post(newAddressToPost: AddressModel, userCode: string) {
        newAddressToPost.userCode = userCode;
        this.http.post<String>(environment.hostAddress + "address/editAddress/" + userCode, newAddressToPost).subscribe(res => {
            const modalRef = this.modalService.open(SuccessModalComponent);
            modalRef.componentInstance.title = "New address added!";
            modalRef.result.then(() => {
                location.reload();
            }, () => {
                location.reload();
            })
        });
    }

    delete(userCode: string, addressCode: string) {
        this.http.delete(environment.hostAddress + "address/editAddress/" + userCode + "/" + addressCode).subscribe(res => {
            const modalRef = this.modalService.open(SuccessModalComponent);
            modalRef.componentInstance.title = "Address deleted!";
            modalRef.result.then(() => {
                location.reload();
            }, () => {
                location.reload();
            })
        });
    }
}
