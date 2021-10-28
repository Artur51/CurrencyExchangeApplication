import { Component, OnInit } from '@angular/core';
import { UserCredentials } from 'src/app/pojo/Pojo';
import { Utils } from 'src/app/utils/Utils';
import { AuthLoginService } from '../../../services/auth-login.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
})
export class LoginFormComponent implements OnInit {
  public credentials = new UserCredentials();

  constructor(public auth: AuthLoginService) {}

  ngOnInit(): void {}
  login() {
    this.auth.login(this.credentials);
  }

  public isFieldValid(state: any) {
    return Utils.isFieldValid(state);
  }

  public isFormValid(state: any) {
    return Utils.isFormValid(state);
  }
}
