import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MustMatch } from '../../helpers/must-match';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  submitted: boolean;

  constructor(private formBuilder: FormBuilder) {

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
    console.log("correct form");
  }

}
