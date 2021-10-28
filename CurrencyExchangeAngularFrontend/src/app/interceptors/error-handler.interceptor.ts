import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { ErrorEventData } from '../pojo/Pojo';
import { catchError } from 'rxjs/operators';
import { AuthLoginService } from '../services/auth-login.service';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  constructor(private auth: AuthLoginService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage: ErrorEventData;
        if (error.error instanceof ErrorEvent) {
          // client-side error
          errorMessage = new ErrorEventData('Error', error.error.message);
        } else {
          // server-side error
          errorMessage = new ErrorEventData(
            error.error.error,
            error.error.message,
            error.status
          );
        }
        if (
          errorMessage.status === 0 &&
          !errorMessage.errorCapture &&
          !errorMessage.message
        ) {
          errorMessage.errorCapture = 'Server connection issue.';
          errorMessage.message = 'We are sorry. Service currenlty unavailable.';
        }

        this.auth.error$.next(errorMessage);
        return throwError(error);
      })
    );
  }

  public static getProvider() {
    return [
      {
        provide: HTTP_INTERCEPTORS,
        useClass: ErrorHandlerInterceptor,
        multi: true,
      },
    ];
  }
}
