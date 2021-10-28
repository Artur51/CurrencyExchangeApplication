import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ErrorEventData, UserCredentials } from '../pojo/Pojo';
import { Utils } from '../utils/Utils';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthLoginService {
  public backendUrl = environment.backendUrl;
  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService,
    private router: Router
  ) {
    this.isLogined$.subscribe((value) => {
      this.isLogined = value;
    });

    if (this.tokenStorage.getToken()) {
      this.isLogined$.next(true);
    }

    this.error$.subscribe((error) => {
      if (error && error.errorCapture)
        alert(error.errorCapture + '\n\n' + error.message);
    });
  }

  public isLogined$ = new BehaviorSubject<boolean>(false);
  public isLogined = false;

  public error$ = new BehaviorSubject<ErrorEventData | null>(null);
  public getUserName(): string {
    return this.tokenStorage.getUsername() || '';
  }

  login(credentials: UserCredentials) {
    this.http
      .post(this.backendUrl + 'login', credentials.toFormData())
      .subscribe((response: any) => {
        this.tokenStorage.saveUser(response);
        this.isLogined$.next(true);
        this.router.navigateByUrl('/exchange');
      });
  }

  registration(credentials: UserCredentials) {
    this.http
      .post(this.backendUrl + 'registration', credentials.toFormData())
      .subscribe((response) => {
        this.router.navigateByUrl('/login');
      });
  }

  logout() {
    this.http
      .get(this.backendUrl + 'logout') //
      .subscribe(
        (result) => this.onLogout(),
        (error) => this.onLogout()
      );
  }

  private onLogout() {
    this.tokenStorage.signOut();
    this.isLogined$.next(false);
    this.router.navigateByUrl('/login');
  }
}
