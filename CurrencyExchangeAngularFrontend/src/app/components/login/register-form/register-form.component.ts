import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { UserCredentials } from 'src/app/pojo/Pojo';
import { AuthLoginService } from 'src/app/services/auth-login.service';
import { SecondaryPasswordValidator } from 'src/app/directives/SecondaryPasswordValidator';
import { Utils } from 'src/app/utils/Utils';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss'],
})
export class RegisterFormComponent implements OnInit {
  public credentials = new UserCredentials();

  constructor(private auth: AuthLoginService) {}

  ngOnInit(): void {}
  register() {
    this.auth.registration(this.credentials);
  }

  public isFieldValid(state: any) {
    return Utils.isFieldValid(state);
  }

  public isFormValid(state: any) {
    return Utils.isFormValid(state);
  }
}
