import { Component, Input, OnInit } from '@angular/core';
import { CurrencyExchangeDataService } from 'src/app/services/currency-exchange-data.service';
import { CurrencyExchangeRate } from '../../../pojo/Pojo';

@Component({
  selector: 'app-exchange-rates-info',
  templateUrl: './exchange-rates-info.component.html',
  styleUrls: ['./exchange-rates-info.component.scss'],
})
export class ExchangeRatesInfoComponent implements OnInit {
  constructor(public exchangeService: CurrencyExchangeDataService) {}

  @Input() data: CurrencyExchangeRate[];

  ngOnInit(): void {}

  getExchangeRates() {
    return this.data;
  }

  drawIndex: number;
  resetDrawIndex() {
    this.drawIndex = 0;
  }

  isDrawIndexOdd() {
    return this.drawIndex % 2;
  }
  incrementDrawIndex() {
    this.drawIndex++;
  }

  getIndexes() {
    return Array.from(Array(4).keys());
  }
}
