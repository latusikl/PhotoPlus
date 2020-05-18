import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { User } from '../../models/user/user';
import { UserService } from '../../services/user/user.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { UserFormService } from '../../services/user/user-form.service';


@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    registerForm: FormGroup;
    submitted: boolean;

    constructor(private userService: UserService,
                private modalService: NgbModal,
                private userFormService: UserFormService) {}

    ngOnInit(): void {
        this.registerForm = this.userFormService.generateUserForm();
    }

    get f() {
        return this.registerForm.controls;
    }

    onSubmit() {
        this.submitted = true;
        if (this.registerForm.invalid) {
            return;
        }

        const user: User = this.userFormService.getUserFromUserForm(this.registerForm);

        this.userService.registerUser(user).subscribe(result => {
            const modalRef = this.modalService.open(SuccessModalComponent);
            modalRef.componentInstance.message = 'Welcome ' + user.name + ' ' + user.surname + '!';
            modalRef.componentInstance.title = 'Registered successfully!';
        });
    }

}
