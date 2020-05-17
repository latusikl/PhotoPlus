import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UserFormService } from '../../../services/user/user-form.service';
import { LoginService } from '../../../services/login/login.service';
import { UserService } from '../../../services/user/user.service';
import { User } from '../../../models/user/user';
import { FieldChange } from '../../../services/patch/field-change';

@Component({
    selector: 'app-user-data',
    templateUrl: './user-data.component.html',
    styleUrls: ['../user-details.component.scss']
})
export class UserDataComponent implements OnInit {

    constructor(private userFormService: UserFormService, private loginService: LoginService, private userService: UserService) {
    }

    userForm: FormGroup;
    formDisabled = true;
    submitted = false;
    changedFields = new FieldChange();

    PASSWORD_FILLER_LENGTH = 10;

    currentUser: User;

    ngOnInit(): void {
        this.userForm = this.userFormService.generateUserForm();
        this.userForm.disable();
        this.userService.getDetailsOfLoggedUser().subscribe((res: User) => {
            this.currentUser = res;
            this.fillForm();
        });
    }

    fillForm(): void {
        this.userForm.controls.login.setValue(this.currentUser.login);
        this.userForm.controls.mail.setValue(this.currentUser.email);
        this.userForm.controls.name.setValue(this.currentUser.name);
        this.userForm.controls.surname.setValue(this.currentUser.surname);
        this.userForm.controls.phoneNumber.setValue(this.currentUser.number);
        this.userForm.controls.password.setValue(this.getPasswordFiller());
        this.userForm.controls.confirmPass.setValue(this.getPasswordFiller());
    }

    getPasswordFiller(): string {
        return this.currentUser.password.substr(0, this.PASSWORD_FILLER_LENGTH);
    }

    onSubmit() {
        this.submitted = true;
        if (this.userForm.invalid) {
            return;
        }
        this.userService.patchDetailsOfLoggedUser(this.changedFields);
        this.userForm.disable();
    }

    registerChange(key: string, value: string): void {
        this.changedFields.registerChange(key, value);
    }

    disable() {
        this.userForm.disable();
        this.formDisabled = true;
    }

    enable() {
        this.userForm.enable();
        this.formDisabled = false;
    }

    get f() {
        return this.userForm.controls;
    }
}
