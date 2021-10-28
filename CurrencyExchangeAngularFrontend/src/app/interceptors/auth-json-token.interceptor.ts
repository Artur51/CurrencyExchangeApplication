import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthLoginService } from '../services/auth-login.service';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable()
export class AuthJsonTokenInterceptor implements HttpInterceptor {
  constructor(private tokenStorage: TokenStorageService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.tokenStorage.getToken();
    if (token != null) {
      req = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + token),
      });
    }
    return next.handle(req);
  }

  public static getProvider() {
    return [
      {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthJsonTokenInterceptor,
        multi: true,
      },
    ];
  }
}
