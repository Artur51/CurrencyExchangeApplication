import { Component, Input, OnInit } from '@angular/core';
import { ExchangeCurrencyLogEvent } from 'src/app/pojo/Pojo';
import { CurrencyExchangeDataService } from '../../../services/currency-exchange-data.service';

@Component({
  selector: 'app-currencies-logs',
  templateUrl: './currencies-logs.component.html',
  styleUrls: ['./currencies-logs.component.scss'],
})
export class CurrenciesLogsComponent implements OnInit {
  constructor(private exchangeService: CurrencyExchangeDataService) {}

  @Input() data: ExchangeCurrencyLogEvent[];

  getData() {
    return this.data;
  }

  isDataAvailable() {
    return this.data && this.data.length > 0;
  }

  ngOnInit(): void {}
}
