import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MustMatch } from '../../helpers/must-match';
import { User } from '../../models/user/user';
import { UserService } from "../../services/user/user.service";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorModel } from 'src/app/models/error/errormodel';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { Role } from 'src/app/models/role/role.enum';


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  submitted: boolean;

  constructor(private formBuilder: FormBuilder, private userService: UserService,
    private modalService: NgbModal) {

   }

  ngOnInit(): void {
   this.registerForm = this.formBuilder.group({
    login: ['',[Validators.required, Validators.minLength(5)]],
    mail: ['', [Validators.required, Validators.email ]],
    password: ['', [ Validators.required, Validators.minLength(5) ]],
    confirmPass : ['', [Validators.required]],
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
    surname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
    phoneNumber: ['',  Validators.pattern(new RegExp("[1-9][0-9]{2}-[0-9]{3}-[0-9]{3}"))]
   }, {
     validator: [MustMatch('password', 'confirmPass')]
    });
  }

  get f() { return this.registerForm.controls; }

  onSubmit(){
    this.submitted = true;
    if(this.registerForm.invalid){
      return;
    }

    const alias = this.registerForm.value;
    const user: User = {
      login: alias.login,
      email: alias.mail,
      password: alias.password,
      name: alias.name,
      surname: alias.surname,
      number: alias.phoneNumber,
      code: null,
      role: Role.CLIENT
    };

    this.userService.post(user).subscribe(result => {
      const modalRef = this.modalService.open(SuccessModalComponent);
      modalRef.componentInstance.message = "Welcome " + user.name + " " + user.surname + "!";
      modalRef.componentInstance.title = "Registered successfully!";
    });
  }

}
