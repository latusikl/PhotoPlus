import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { User } from '../../models/user/user';
import { UserService } from "../../services/user/user.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  submitted: boolean;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {

  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
    login: ['',[Validators.required]],
    password: ['', [ Validators.required ]]
   });
  }

  get f() { return this.loginForm.controls; }

  onSubmit(){
    this.submitted = true;
    if(this.loginForm.invalid){
      return;
    }
  }
}
