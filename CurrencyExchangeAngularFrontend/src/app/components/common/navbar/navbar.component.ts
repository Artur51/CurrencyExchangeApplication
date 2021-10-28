import { Component, OnInit } from '@angular/core';
import { AuthLoginService } from '../../../services/auth-login.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  constructor(public auth: AuthLoginService) {}

  ngOnInit(): void {}

  logout() {
    this.auth.logout();
  }
}
