import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MustMatch } from '../../helpers/must-match';
import { User } from '../../models/user/user';
import { UserService } from "../../services/user/user.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  submitted: boolean;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {

   }

  ngOnInit(): void {
   this.registerForm = this.formBuilder.group({
    login: ['',[Validators.required, Validators.minLength(5)]],
    mail: ['', [Validators.required, Validators.email ]],
    password: ['', [ Validators.required ]],
    confirmPass : ['', [Validators.required]],
    name: ['', [Validators.required]],
    surname: ['', [Validators.required]],
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
      number: alias.phoneNumber
    };

    console.log(user);
    const preparedRequest = this.userService.post(user);
    console.log(preparedRequest);
    let responseObject = null
    preparedRequest.subscribe((x) => responseObject = x);
    console.log(responseObject);


  }

}
