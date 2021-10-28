import { Pipe, PipeTransform } from '@angular/core';
import { CurrencyExchangeRate } from '../pojo/Pojo';

@Pipe({
  name: 'excludeEuroCurrency',
})
export class ExcludeEuroCurrencyPipe implements PipeTransform {
  transform(
    value: CurrencyExchangeRate[],
    ...args: unknown[]
  ): CurrencyExchangeRate[] {
    return value.filter((a) => !a.inExchangeListHidden);
  }
}
