import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MustMatch } from "../../helpers/must-match";
import { User } from "../../models/user/user";
import { Role } from "../../models/role/role.enum";

@Injectable({
  providedIn: 'root'
})
export class UserFormService {

  constructor(private formBuilder: FormBuilder) {
  }

  public generateUserForm(): FormGroup {
    return this.formBuilder.group({
      login: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(12)]],
      mail: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
      confirmPass: ['', [Validators.required]],
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
      surname: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]],
      phoneNumber: ['', Validators.pattern(new RegExp("[1-9][0-9]{2}-[0-9]{3}-[0-9]{3}"))]
    }, {
      validator: [MustMatch('password', 'confirmPass')]
    });

  }

  public getUserFromUserForm(registrationForm: FormGroup): User {
    const alias = registrationForm.value;
    const user: User = {
      login: alias.login,
      email: alias.mail,
      password: alias.password,
      name: alias.name,
      surname: alias.surname,
      number: alias.phoneNumber,
      role: Role.CLIENT,
      code: null
    };
    return user;
  }


}
