import { ExcludeEuroCurrencyPipe } from './exclude-euro-currency.pipe';
import * as mockExchangeInfoResponse from '../../../mockResponse/mockExchangeInfoResponse.json';
import { CurrencyExchangeTimeAndRates } from '../pojo/Pojo';
describe('ExcludeEuroCurrencyPipe', () => {
  it('create an instance', () => {
    const pipe = new ExcludeEuroCurrencyPipe();
    expect(pipe).toBeTruthy();
  });
});

describe('ExcludeEuroCurrencyPipe should remove currency with 1.0 rate', () => {
  it('create an instance', () => {
    const pipe = new ExcludeEuroCurrencyPipe();
    expect(pipe).toBeTruthy();
    mockExchangeInfoResponse as any as CurrencyExchangeTimeAndRates;
    let result = pipe.transform(mockExchangeInfoResponse.exchangeRates);

    expect(result.filter((a) => !a.inExchangeListHidden)).toHaveSize(0);
  });
});
