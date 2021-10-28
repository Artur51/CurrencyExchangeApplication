import { Component, OnInit } from '@angular/core';
import { CurrencyExchangeDataService } from '../../../services/currency-exchange-data.service';

@Component({
  selector: 'app-currencies-logs-page',
  templateUrl: './currencies-logs-page.component.html',
  styleUrls: ['./currencies-logs-page.component.scss'],
})
export class CurrenciesLogsPageComponent implements OnInit {
  constructor(private exchangeService: CurrencyExchangeDataService) {}
  isFilterShow = false;
  getData() {
    return this.exchangeService.exchangeLogs$.getValue().data!.content;
  }
  showFilterPopup() {
    this.isFilterShow = !this.isFilterShow;
  }
  ngOnInit(): void {}
}
