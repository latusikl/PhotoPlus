import { environment } from "../../../environments/environment";
import { SuccessModalComponent } from "../../components/success-modal/success-modal.component";
import { HttpClient } from "@angular/common/http";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Injectable } from "@angular/core";
import { FieldChange } from "./field-change";

@Injectable({
    providedIn: 'root'
})
export class PatchFieldChangeService {

    constructor(private http: HttpClient, private modalService: NgbModal) {
    }

    getNewFieldChange(): FieldChange {
        return new FieldChange();
    }

    sendPatchRequest(endpoint: string, fieldChange: FieldChange): void {
        let changedFields = fieldChange.map;
        if (changedFields.size != 0) {
            this.http.patch(environment.hostAddress + endpoint, JSON.parse(this.getJsonString(changedFields))).subscribe(res => {
                const modalRef = this.modalService.open(SuccessModalComponent);
                modalRef.componentInstance.title = "Your's data has been changed!";
                modalRef.result.then(() => {
                    location.reload();
                }, () => {
                    location.reload();
                })
            });
        }
    }

    private getJsonString(changedFields: Map<string, string>): string {
        let jsonObject = {};
        changedFields.forEach((value, key) => {
            jsonObject[key] = value
        });
        return JSON.stringify(jsonObject);
    }

}
