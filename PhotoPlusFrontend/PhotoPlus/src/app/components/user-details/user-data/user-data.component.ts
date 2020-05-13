import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {UserModel} from "../../../models/user/user-model";
import {UserFormService} from "../../../services/user/user-form.service";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {SuccessModalComponent} from "../../success-modal/success-modal.component";

@Component({
    selector: 'app-user-data',
    templateUrl: './user-data.component.html',
    styleUrls: ['../user-details.component.scss']
})
export class UserDataComponent implements OnInit {

    constructor(private userFormService: UserFormService, private  http: HttpClient, private modalService: NgbModal) {
    }

    userForm: FormGroup;
    formDisabled : boolean = true;
    submitted: boolean = false;
    fillerLength: number = 10;
    changedFields: Map<string, string> = new Map<string, string>();

    @Input("loggedUser")
    currentUser: UserModel;

    ngOnInit(): void {
        this.userForm = this.userFormService.generateUserForm();
        this.userForm.disable();
        this.fillForm();
    }

    fillForm(): void {
        this.userForm.controls["login"].setValue(this.currentUser.login);
        this.userForm.controls["mail"].setValue(this.currentUser.email);
        this.userForm.controls["name"].setValue(this.currentUser.name);
        this.userForm.controls["surname"].setValue(this.currentUser.surname);
        this.userForm.controls["phoneNumber"].setValue(this.currentUser.number);
        this.userForm.controls["password"].setValue(this.getPasswordFiller());
        this.userForm.controls["confirmPass"].setValue(this.getPasswordFiller());
    }

    getPasswordFiller(): string {
        return this.currentUser.password.substr(0, this.fillerLength);
    }

    onSubmit() {
        this.submitted = true;
        if (this.userForm.invalid) {
            return;
        }
        this.sendPatchRequest();
        this.userForm.disable();
    }

    registerChange(key: string, value: string): void {
        if (this.changedFields.has(key)) {
            this.changedFields.delete(key);
        }
        this.changedFields.set(key, value);
    }

    sendPatchRequest(): void {
        if (this.changedFields.size != 0) {
            this.http.patch(environment.hostAddress + 'user/editAccount/' + this.currentUser.code, JSON.parse(this.getJsonString())).subscribe(res => {
                const modalRef = this.modalService.open(SuccessModalComponent);
                modalRef.componentInstance.message = "Your's data chas been changed.";
            });
            this.changedFields.clear();
        }
    }

    getJsonString(): string {
        let jsonObject = {};
        this.changedFields.forEach((value, key) => {
            jsonObject[key] = value
        });
        return JSON.stringify(jsonObject);
    }

    disable(){
        this.userForm.disable();
        this.formDisabled=true;
    }

    enable(){
        this.userForm.enable();
        this.formDisabled=false;
    }

    get f() {
        return this.userForm.controls;
    }
}
