import { Component, OnInit } from '@angular/core';
import { CurrencyExchangeDataService } from '../../../services/currency-exchange-data.service';

@Component({
  selector: 'app-exchange-currenicies-page',
  templateUrl: './exchange-currencies-page.component.html',
  styleUrls: ['./exchange-currencies-page.component.scss'],
})
export class ExchangeCurrenciesPageComponent implements OnInit {
  constructor(private exchangeService: CurrencyExchangeDataService) {}

  ngOnInit(): void {}

  getDate() {
    return this.exchangeService.exchangeRates$.getValue().time;
  }

  getHistoryData() {
    return this.exchangeService.exchangeLocalHistory$.getValue();
  }

  isInfoAvailable() {
    return !this.exchangeService.exchangeRates$.getValue().notAvailable;
  }

  getExchangeRates() {
    const extracted = this.exchangeService.exchangeRates$.getValue();
    return extracted.exchangeRates;
  }
}
