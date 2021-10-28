import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription, timer } from 'rxjs';
import { CurrencyExchangeRequest } from 'src/app/pojo/Pojo';
import { CurrencyExchangeDataService } from 'src/app/services/currency-exchange-data.service';

@Component({
  selector: 'app-exchange-currencies',
  templateUrl: './exchange-currencies.component.html',
  styleUrls: ['./exchange-currencies.component.scss'],
})
export class ExchangeCurrenciesComponent implements OnInit {
  exchange = new CurrencyExchangeRequest();

  constructor(public exchangeService: CurrencyExchangeDataService) {}

  ngOnInit(): void {}

  getExchangeToAmount() {
    return this.exchangeService.getExchangeAmount(this.exchange);
  }

  getCurrencis() {
    let currencies = this.exchangeService.getCurrencies();
    if (!this.isBothSelected()) currencies.unshift('');
    return currencies;
  }

  isReadyToExchange() {
    return this.exchange.isFieldsFilled();
  }
  isAnySelected() {
    return (
      this.exchange.currencySold !== '' || this.exchange.currencyPurchase !== ''
    );
  }
  isBothSelected() {
    return this.exchange.currencySold && this.exchange.currencyPurchase;
  }

  onChangeFrom(currency: any) {
    this.maybeSwitchOtherCurrency(currency, this.exchange.currencySold);
    this.exchange.currencySold = currency;
  }
  onChangeTo(currency: any) {
    this.maybeSwitchOtherCurrency(currency, this.exchange.currencyPurchase);
    this.exchange.currencyPurchase = currency;
  }

  maybeSwitchOtherCurrency(selected: string, valuePendingChange: string) {
    let other =
      valuePendingChange === this.exchange.currencyPurchase
        ? this.exchange.currencySold
        : this.exchange.currencyPurchase;
    if (other === selected) {
      if (valuePendingChange === this.exchange.currencyPurchase) {
        this.exchange.currencySold = this.exchange.currencyPurchase;
      } else {
        this.exchange.currencyPurchase = this.exchange.currencySold;
      }
    }
  }

  makeExchange() {
    let exchangeCopy = new CurrencyExchangeRequest(this.exchange);
    this.exchange.reset();
    this.exchangeService.makeExchange(exchangeCopy);
  }
}
