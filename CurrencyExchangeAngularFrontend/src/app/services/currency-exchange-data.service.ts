import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {
  CurrencyExchangeRequest,
  CurrencyExchangeTimeAndRates,
  ErrorEventData,
  ExchangeCurrencyLogEvent,
  ExchangeCurrencyLogEventQueryData,
  ExchangeCurrencyLogEvents,
  PageRequestData,
} from '../pojo/Pojo';
import { Utils } from '../utils/Utils';
import { AuthLoginService } from './auth-login.service';
import { Location } from '@angular/common';
@Injectable({
  providedIn: 'root',
})
export class CurrencyExchangeDataService {
  getCurrencies() {
    return this.exchangeRates$.getValue().exchangeRates.map((a) => a.currency);
  }
  private localHistoryCount = 10;

  public exchangeLocalHistory$ = new BehaviorSubject<
    ExchangeCurrencyLogEvent[]
  >([]);

  public exchangeLogs$ = new BehaviorSubject<ExchangeCurrencyLogEvents>(
    ExchangeCurrencyLogEvents.emptyInstance
  );

  public exchangeRates$ = new BehaviorSubject<CurrencyExchangeTimeAndRates>(
    CurrencyExchangeTimeAndRates.emptyInstance
  );

  constructor(
    private auth: AuthLoginService,
    private http: HttpClient,
    private location: Location
  ) {
    auth.isLogined$.subscribe((a) => (a ? this.requestExchangeRates() : ''));
    auth.isLogined$.subscribe((a) => (a ? this.requestExchangeLogs() : ''));

    location.onUrlChange((url) => {
      if (auth.isLogined) {
        if (url === '/logs') {
          this.requestExchangeLogs();
        }
      }
    });
  }

  makeExchange(data: CurrencyExchangeRequest) {
    this.http
      .post<ExchangeCurrencyLogEvent>(
        this.auth.backendUrl + 'makeExchange',
        data.toFormData()
      )
      .subscribe((response) => {
        this.exchangeLocalHistory$.getValue().push(response);
        this.trimLocalHistory();
      });
  }

  trimLocalHistory() {
    const items = this.exchangeLocalHistory$.getValue();
    Utils.removeFromStart(items, this.localHistoryCount);
  }

  private getRate(currencyName: string): number {
    const rate = this.exchangeRates$
      .getValue()
      .exchangeRates.find((a) => a.currency === currencyName)?.rate;
    return rate ? rate : 0.00001;
  }

  private requestExchangeRates() {
    this.http
      .get<CurrencyExchangeTimeAndRates>(this.auth.backendUrl + 'exchangeInfo')
      .subscribe(
        (response) => {
          this.exchangeRates$.next(response);
        },
        (error) => {
          this.exchangeRates$.next(CurrencyExchangeTimeAndRates.emptyInstance);
        }
      );
  }

  public requestExchangeLogs(queryData?: ExchangeCurrencyLogEventQueryData) {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let pageRequestData = new PageRequestData();
    pageRequestData.pageSize = 10;
    // pageRequestData.offset = 1;

    this.http
      .post<ExchangeCurrencyLogEvents>(
        this.auth.backendUrl + 'exchangeLogs',
        {
          queryData,
          pageRequestData,
        },
        { headers }
      )
      .subscribe(
        (response) => {
          this.exchangeLogs$.next(response);
        },
        (error) => {
          this.exchangeLogs$.next(ExchangeCurrencyLogEvents.emptyInstance);
        }
      );
  }

  getExchangeAmount(data: CurrencyExchangeRequest) {
    let value = data.currencyAmountSold;
    // methods from euro and to euro are switched to have better percision
    value *= this.getRate(data.currencyPurchase); // from euro
    value /= this.getRate(data.currencySold); // to euro
    return value;
  }
}
