import { TestBed } from '@angular/core/testing';

import { AuthJsonTokenInterceptor } from './auth-json-token.interceptor';

describe('AuthCredentialsInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      AuthJsonTokenInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: AuthJsonTokenInterceptor = TestBed.inject(AuthJsonTokenInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
