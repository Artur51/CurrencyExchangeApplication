import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { ExchangeCurrencyLogEventQueryData } from 'src/app/pojo/Pojo';
import { CurrencyExchangeDataService } from 'src/app/services/currency-exchange-data.service';

@Component({
  selector: 'app-filter-form',
  templateUrl: './filter-form.component.html',
  styleUrls: ['./filter-form.component.scss'],
})
export class FilterFormComponent {
  model = new ExchangeCurrencyLogEventQueryData();

  constructor(public exchangeService: CurrencyExchangeDataService) {}

  getCurrencis() {
    let values = this.exchangeService.getCurrencies();
    values.unshift('');
    return values;
  }

  reset() {
    this.model.reset();
  }

  public sortNames = ['Created at', 'currency sold', 'currency received'];
  public sortValues = [
    'createdAt',
    'currencyAmountSold',
    'currencyAmountReceived',
  ];

  onOrderBySelect(value: any) {
    this.model.orderBy = value;
  }

  getSortTypes() {
    return this.sortNames;
  }

  onSubmit() {
    const item = new ExchangeCurrencyLogEventQueryData(this.model);
    let sortName = item.orderBy;
    if (sortName) {
      let id = this.sortNames.indexOf(sortName);
      if (id >= 0) {
        sortName = this.sortValues[id];
        item.orderBy = sortName;
      }
    }
    this.exchangeService.requestExchangeLogs(item);
  }

  onCurrencySoldSelected($event: string) {
    this.model.currencySold = $event;
  }
  onCurrencyPurchaseSelected($event: string) {
    this.model.currencyPurchase = $event;
  }

  onChangeMinCurrencySold($event: any) {
    this.model.currencyAmountSoldMin = $event;
  }
  onChangeMaxCurrencySold($event: any) {
    this.model.currencyAmountSoldMax = $event;
  }
  onChangeMinCurrencyPurchased($event: any) {
    this.model.currencyAmountReceivedMin = $event;
  }
  onChangeMaxCurrencyPurchased($event: any) {
    this.model.currencyAmountReceivedMax = $event;
  }
}
