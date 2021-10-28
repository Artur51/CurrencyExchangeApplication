import { TestBed } from '@angular/core/testing';
import { CurrencyExchangeTimeAndRates } from '../pojo/Pojo';

import { CurrencyExchangeDataService } from './currency-exchange-data.service';


describe('CurrencyExchangeDataService', () => {
  let service: CurrencyExchangeDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurrencyExchangeDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
